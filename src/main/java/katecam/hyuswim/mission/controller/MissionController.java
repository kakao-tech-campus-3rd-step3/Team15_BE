package katecam.hyuswim.mission.controller;

import java.util.List;

import katecam.hyuswim.mission.dto.UserMissionStatsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.mission.dto.MissionStatsResponse;
import katecam.hyuswim.mission.dto.MissionTodayResponse;
import katecam.hyuswim.mission.service.MissionService;
import katecam.hyuswim.user.domain.User;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

  private final MissionService missionService;

  public MissionController(MissionService missionService) {
    this.missionService = missionService;
  }

  @GetMapping("/today")
  public ResponseEntity<List<MissionTodayResponse>> getToday(@LoginUser User loginUser) {
    return ResponseEntity.ok(missionService.getTodayMissionsWithState(loginUser.getId()));
  }

  // 미션 시작
  @PatchMapping("/{missionId}/start")
  public ResponseEntity<Void> startMission(
      @LoginUser User loginUser, @PathVariable Long missionId) {
    missionService.startMission(loginUser.getId(), missionId);
    return ResponseEntity.ok().build();
  }

  // 미션 완료
  @PatchMapping("/{missionId}/complete")
  public ResponseEntity<Void> completeMission(
      @LoginUser User loginUser, @PathVariable Long missionId) {
    missionService.completeMission(loginUser.getId(), missionId);
    return ResponseEntity.ok().build();
  }

  // 오늘 통계 조회 (참여 인원, 완료 인원)
  @GetMapping("/{missionId}/stats/today")
  public ResponseEntity<MissionStatsResponse> getTodayStats(@PathVariable Long missionId) {
    return ResponseEntity.ok(missionService.getTodayStats(missionId));
  }

    @GetMapping("/stats")
    public ResponseEntity<UserMissionStatsResponse> getUserStats(@LoginUser User loginUser) {
        return ResponseEntity.ok(missionService.getUserStats(loginUser));
    }
}
