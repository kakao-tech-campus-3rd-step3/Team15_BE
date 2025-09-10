package katecam.hyuswim.mission.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.mission.service.MissionService;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

  private final MissionService missionService;

  public MissionController(MissionService missionService) {
    this.missionService = missionService;
  }

  // 미션 시작
  @PostMapping("/{missionId}/start")
  public ResponseEntity<Void> startMission(
      @RequestParam Long userId, @PathVariable Long missionId) {
    missionService.startMission(userId, missionId);
    return ResponseEntity.ok().build();
  }

  // 미션 완료
  @PostMapping("/{missionId}/complete")
  public ResponseEntity<Void> completeMission(
      @RequestParam Long userId, @PathVariable Long missionId) {
    missionService.completeMission(userId, missionId);
    return ResponseEntity.ok().build();
  }

  // 오늘 통계 조회 (참여 인원, 완료 인원)
  @GetMapping("/{missionId}/stats/today")
  public ResponseEntity<MissionService.MissionStats> getTodayStats(@PathVariable Long missionId) {
    return ResponseEntity.ok(missionService.getTodayStats(missionId));
  }
}
