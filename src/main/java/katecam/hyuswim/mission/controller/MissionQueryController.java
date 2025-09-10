// package katecam.hyuswim.mission.controller;
package katecam.hyuswim.mission.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.mission.dto.MissionTodayDto;
import katecam.hyuswim.mission.service.MissionQueryService;

@RestController
@RequestMapping("/api/missions")
public class MissionQueryController {

    private final MissionQueryService missionQueryService;

    public MissionQueryController(MissionQueryService missionQueryService) {
        this.missionQueryService = missionQueryService;
    }

    // 오늘용 목록 + 사용자 상태 + 통계
    @GetMapping("/today")
    public ResponseEntity<List<MissionTodayDto>> getToday(@RequestParam Long userId) {
        return ResponseEntity.ok(missionQueryService.getTodayMissionsWithState(userId));
    }
}
