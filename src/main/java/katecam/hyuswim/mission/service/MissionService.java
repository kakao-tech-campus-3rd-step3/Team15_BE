// src/main/java/katecam/hyuswim/mission/service/MissionService.java
package katecam.hyuswim.mission.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    long exists = missionProgressRepository.countByUserIdAndProgressDate(userId, today);
    if (exists > 0) throw new IllegalStateException("하루 1회 제한");

    MissionProgress p = new MissionProgress();
    p.setUser(user);
    p.setMission(mission);
    p.setProgressDate(today);
    p.setStartedAt(LocalDateTime.now());
    p.setIsCompleted(false);

    try {
      missionProgressRepository.save(p);
    } catch (DataIntegrityViolationException e) {
      throw new IllegalStateException("하루 1회 제한(동시 요청)", e);
    }
  }

  public void completeMission(Long userId, Long missionId) {
    LocalDate today = LocalDate.now();
    MissionProgress progress =
        missionProgressRepository
            .findFirstByUserIdAndMissionIdAndProgressDate(userId, missionId, today)
            .orElseThrow(() -> new IllegalStateException("오늘 시작 기록 없음"));

    if (Boolean.TRUE.equals(progress.getIsCompleted())) {
      throw new IllegalStateException("이미 완료");
    }

    progress.setIsCompleted(true);
    progress.setCompletedAt(LocalDateTime.now());
  }

  // ===== Queries =====
  @Transactional(readOnly = true)
  public MissionStatsResponse getTodayStats(Long missionId) {
    LocalDate today = LocalDate.now();
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
    if (todayProgressForUser == null) return TodayState.NOT_STARTED;
    boolean sameMission = todayProgressForUser.getMission().getId().equals(m.getId());
    if (sameMission && Boolean.TRUE.equals(todayProgressForUser.getIsCompleted()))
      return TodayState.COMPLETED;
    if (sameMission) return TodayState.IN_PROGRESS;
    return TodayState.NOT_STARTED; // 다른 미션을 이미 진행 중/완료한 경우
  }
}
