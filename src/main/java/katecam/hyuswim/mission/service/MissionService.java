package katecam.hyuswim.mission.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.mission.TodayState;
import katecam.hyuswim.mission.dto.MissionStatsResponse;
import katecam.hyuswim.mission.dto.MissionTodayResponse;
import katecam.hyuswim.mission.dto.UserMissionStats;
import katecam.hyuswim.mission.event.MissionCompletedEvent;
import katecam.hyuswim.mission.progress.MissionProgress;
import katecam.hyuswim.mission.repository.MissionProgressRepository;
import katecam.hyuswim.mission.repository.MissionRepository;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {

    private static final int DAILY_LIMIT = 1;

    private final MissionRepository missionRepository;
    private final MissionProgressRepository missionProgressRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    // ===== Commands =====

    public void startMission(Long userId, Long missionId) {
        var user = userRepository.findById(userId).orElseThrow();
        var mission = missionRepository.findById(missionId).orElseThrow();

        if (!mission.isActive()) throw new IllegalStateException("비활성 미션");

        LocalDate today = LocalDate.now();
        if (!mission.isAvailableOn(today)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MISSION_UNAVAILABLE_TODAY");
        }

        if (missionProgressRepository.countByUserIdAndProgressDate(userId, today) >= DAILY_LIMIT) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "DAILY_LIMIT_REACHED");
        }

        try {
            missionProgressRepository.saveAndFlush(
                    MissionProgress.startOf(user, mission, today, LocalDateTime.now()));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CONCURRENT_LIMIT_BREACH", e);
        }
    }

    public void completeMission(Long userId, Long missionId) {
        LocalDate today = LocalDate.now();

        MissionProgress progress = missionProgressRepository
                .findFirstByUserIdAndMissionIdAndProgressDate(userId, missionId, today)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "NO_START_RECORD_TODAY"));

        if (progress.getIsCompleted()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ALREADY_COMPLETED");
        }

        var user = progress.getUser();
        var mission = progress.getMission();
        long pointsToAdd = mission.getPoint() == null ? 0 : mission.getPoint();

        progress.complete(LocalDateTime.now());
        user.addPoints(pointsToAdd);
        userRepository.save(user);

        // 이벤트 발행 (배지 지급 로직은 핸들러에서 처리)
        eventPublisher.publishEvent(new MissionCompletedEvent(user.getId()));
    }

    public void cancelMission(Long userId, Long missionId) {
        LocalDate today = LocalDate.now();
        MissionProgress progress = missionProgressRepository
                .findFirstByUserIdAndMissionIdAndProgressDate(userId, missionId, today)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "NO_START_RECORD_TODAY"));

        if (progress.getIsCompleted()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CANNOT_CANCEL_COMPLETED_MISSION");
        }

        missionProgressRepository.delete(progress);
    }

    // ===== Queries =====

    @Transactional(readOnly = true)
    public MissionStatsResponse getTodayStats(Long missionId) {
        var mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MISSION_NOT_FOUND"));

        LocalDate today = LocalDate.now();
        long started = missionProgressRepository.countByMissionIdAndProgressDate(missionId, today);
        long completed = missionProgressRepository
                .countByMissionIdAndProgressDateAndIsCompletedTrue(missionId, today);

        return new MissionStatsResponse(started, completed);
    }

    @Transactional(readOnly = true)
    public List<MissionTodayResponse> getTodayRecommendations(Long userId, int limit) {
        LocalDate today = LocalDate.now();
        var missions = missionRepository.findAll();
        var todayProgressForUser =
                missionProgressRepository.findFirstByUserIdAndProgressDate(userId, today).orElse(null);

        return missions.stream()
                .filter(m -> m.isAvailableOn(today) && m.isActive())
                .map(m -> {
                    long started = missionProgressRepository.countByMissionIdAndProgressDate(m.getId(), today);
                    long completed = missionProgressRepository
                            .countByMissionIdAndProgressDateAndIsCompletedTrue(m.getId(), today);
                    TodayState state = resolveState(todayProgressForUser, m);
                    return MissionTodayResponse.of(m, started, completed, state);
                })
                .filter(r -> r.getState() == TodayState.NOT_STARTED) // 아직 안 한 것만 추천
                .sorted((a, b) -> {
                    long pa = a.getPoint() == null ? 0 : a.getPoint();
                    long pb = b.getPoint() == null ? 0 : b.getPoint();
                    int byPoint = Long.compare(pb, pa); // 점수 높은 순
                    if (byPoint != 0) return byPoint;
                    int la = a.getLevel().ordinal();
                    int lb = b.getLevel().ordinal();
                    return Integer.compare(la, lb); // 난이도 낮은 순
                })
                .limit(limit)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<MissionTodayResponse> getTodayMissionsWithState(Long userId) {
        LocalDate today = LocalDate.now();
        var missions = missionRepository.findAll();
        var todayProgressForUser =
                missionProgressRepository.findFirstByUserIdAndProgressDate(userId, today).orElse(null);

        return missions.stream()
                .map(m -> {
                    long started = missionProgressRepository.countByMissionIdAndProgressDate(m.getId(), today);
                    long completed = missionProgressRepository
                            .countByMissionIdAndProgressDateAndIsCompletedTrue(m.getId(), today);
                    TodayState state = resolveState(todayProgressForUser, m);
                    return MissionTodayResponse.of(m, started, completed, state);
                })
                .toList();
    }

    private TodayState resolveState(MissionProgress progress, Mission mission) {
        if (progress == null) return TodayState.NOT_STARTED;
        boolean sameMission = progress.getMission().getId().equals(mission.getId());
        if (sameMission && progress.getIsCompleted()) return TodayState.COMPLETED;
        if (sameMission) return TodayState.IN_PROGRESS;
        return TodayState.NOT_STARTED;
    }

    @Transactional(readOnly = true)
    public UserMissionStats getUserStats(Long userId) {
        long completed = missionProgressRepository.countByUserIdAndIsCompletedTrue(userId);
        long inProgressToday = missionProgressRepository
                .countByUserIdAndProgressDateAndIsCompletedFalse(userId, LocalDate.now());
        long points = missionProgressRepository.sumCompletedPointsByUser(userId);
        return new UserMissionStats(completed, inProgressToday, points);
    }
}
