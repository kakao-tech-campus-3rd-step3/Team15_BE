// src/main/java/katecam/hyuswim/mission/service/MissionService.java
package katecam.hyuswim.mission.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.mission.TodayState;
import katecam.hyuswim.mission.dto.MissionStatsResponse;
import katecam.hyuswim.mission.dto.MissionTodayResponse;
import katecam.hyuswim.mission.progress.MissionProgress;
import katecam.hyuswim.mission.repository.MissionProgressRepository;
import katecam.hyuswim.mission.repository.MissionRepository;
import katecam.hyuswim.user.repository.UserRepository;

@Service
@Transactional
public class MissionService {
  private static final int DAILY_LIMIT = 1;

  private final MissionRepository missionRepository;
  private final MissionProgressRepository missionProgressRepository;
  private final UserRepository userRepository;

  public MissionService(
      MissionRepository missionRepository,
      MissionProgressRepository missionProgressRepository,
      UserRepository userRepository) {
    this.missionRepository = missionRepository;
    this.missionProgressRepository = missionProgressRepository;
    this.userRepository = userRepository;
  }

  // ===== Commands =====
  public void startMission(Long userId, Long missionId) {
    var user = userRepository.findById(userId).orElseThrow();
    var mission = missionRepository.findById(missionId).orElseThrow();

    if (!mission.isActive()) throw new IllegalStateException("비활성 미션");

    LocalDate today = LocalDate.now();
    if (!mission.isAvailableOn(today)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MISSION_UNAVAILABLE_TODAY");
    }

    long countToday = missionProgressRepository.countByUserIdAndProgressDate(userId, today);
    if (countToday >= DAILY_LIMIT) {
      throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "DAILY_LIMIT_REACHED");
    }

    MissionProgress p = MissionProgress.startOf(user, mission, today, LocalDateTime.now());

    try {
      missionProgressRepository.save(p);
    } catch (DataIntegrityViolationException e) {
      // 동시 요청으로 동일 미션 중복/일일 한도 위반 등
      throw new ResponseStatusException(HttpStatus.CONFLICT, "CONCURRENT_LIMIT_BREACH", e);
    }
  }

  public void completeMission(Long userId, Long missionId) {
    LocalDate today = LocalDate.now();
    MissionProgress progress =
        missionProgressRepository
            .findFirstByUserIdAndMissionIdAndProgressDate(userId, missionId, today)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "NO_START_RECORD_TODAY"));

    if (progress.getIsCompleted()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "ALREADY_COMPLETED");
    }

    progress.complete(LocalDateTime.now());
  }

  @Transactional(readOnly = true)
  public MissionStatsResponse getTodayStats(Long missionId) {
    LocalDate today = LocalDate.now();

    missionRepository
        .findById(missionId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MISSION_NOT_FOUND"));

    long started = missionProgressRepository.countByMissionIdAndProgressDate(missionId, today);
    long completed =
        missionProgressRepository.countByMissionIdAndProgressDateAndIsCompletedTrue(
            missionId, today);
    return new MissionStatsResponse(started, completed);
  }

  @Transactional(readOnly = true)
  public List<MissionTodayResponse> getTodayMissionsWithState(Long userId) {
    LocalDate today = LocalDate.now();
    var missions = missionRepository.findAll();

    var todayProgressForUser =
        missionProgressRepository.findFirstByUserIdAndProgressDate(userId, today).orElse(null);

    return missions.stream()
        .map(
            m -> {
              long started =
                  missionProgressRepository.countByMissionIdAndProgressDate(m.getId(), today);
              long completed =
                  missionProgressRepository.countByMissionIdAndProgressDateAndIsCompletedTrue(
                      m.getId(), today);
              TodayState state = resolveState(todayProgressForUser, m);
              return MissionTodayResponse.of(m, started, completed, state);
            })
        .toList();
  }

  private TodayState resolveState(MissionProgress todayProgressForUser, Mission m) {
    if (todayProgressForUser == null) {
      return TodayState.NOT_STARTED;
    }
    boolean sameMission = todayProgressForUser.getMission().getId().equals(m.getId());
    if (sameMission && todayProgressForUser.getIsCompleted()) {
      return TodayState.COMPLETED;
    }
    if (sameMission) {
      return TodayState.IN_PROGRESS;
    }
    return TodayState.NOT_STARTED;
  }
}
