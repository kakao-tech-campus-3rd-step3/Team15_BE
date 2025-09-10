// src/main/java/katecam/hyuswim/mission/controller/MissionViewController.java
package katecam.hyuswim.mission.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.mission.dto.MissionTodayResponse;
import katecam.hyuswim.mission.dto.TodayState;
import katecam.hyuswim.mission.service.MissionQueryService;
import katecam.hyuswim.mission.service.MissionService;

@Controller
@RequestMapping("/missions")
public class MissionViewController {

    private final MissionQueryService missionQueryService;
    private final MissionService missionService;

    public MissionViewController(
            MissionQueryService missionQueryService, MissionService missionService) {
        this.missionQueryService = missionQueryService;
        this.missionService = missionService;
    }

    private Long currentUserId() {
        return 1L;
    }

    @GetMapping
    public String list(Model model) {
        Long userId = currentUserId();
        List<MissionTodayResponse> list = missionQueryService.getTodayMissionsWithState(userId);

        boolean hasPickedToday =
                list.stream()
                        .anyMatch(m -> m.getState() == TodayState.IN_PROGRESS || m.getState() == TodayState.COMPLETED);

        model.addAttribute("missions", list);
        model.addAttribute("hasPickedToday", hasPickedToday);
        return "missions/list";
    }

    @PostMapping("/{missionId}/start")
    public String start(@PathVariable Long missionId) {
        missionService.startMission(currentUserId(), missionId);
        return "redirect:/missions";
    }

    @PostMapping("/{missionId}/complete")
    public String complete(@PathVariable Long missionId) {
        missionService.completeMission(currentUserId(), missionId);
        return "redirect:/missions";
    }
}
