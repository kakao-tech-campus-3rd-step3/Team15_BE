// src/main/java/katecam/hyuswim/mission/controller/MissionViewController.java
package katecam.hyuswim.mission.controller;

import katecam.hyuswim.mission.MissionCategory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.mission.TodayState;
import katecam.hyuswim.mission.service.MissionService;

@Controller
@RequestMapping("/missions")
public class MissionViewController {

  private final MissionService missionService;

  public MissionViewController(MissionService missionService) {
    this.missionService = missionService;
  }

  private Long currentUserId() {
    return 1L;
  }

    @GetMapping
    public String list(@RequestParam(value = "cat", required = false) MissionCategory cat, Model model) {
        var userId = currentUserId();

        var list = missionService.getTodayMissionsWithState(userId);
        boolean hasPickedToday =
                list.stream().anyMatch(m -> m.getState() == TodayState.IN_PROGRESS || m.getState() == TodayState.COMPLETED);

        var recos = missionService.getTodayRecommendations(userId, 3);
        var stats = missionService.getUserStats(userId);

        model.addAttribute("missions", list);
        model.addAttribute("hasPickedToday", hasPickedToday);
        model.addAttribute("recos", recos);
        model.addAttribute("categories", MissionCategory.values());
        model.addAttribute("activeCat", cat);

        // 상단 요약
        model.addAttribute("completedCount", stats.getCompletedCount());
        model.addAttribute("inProgressCount", stats.getInProgressCount());
        model.addAttribute("earnedPoints", stats.getEarnedPoints());

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
