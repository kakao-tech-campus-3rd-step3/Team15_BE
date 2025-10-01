package katecam.hyuswim.challenge.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.challenge.domain.ChallengeProgress;
import katecam.hyuswim.challenge.service.ChallengeService;

@Controller
@RequestMapping("/challenges")
public class ChallengeViewController {

    private final ChallengeService challengeService;

    public ChallengeViewController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    // 데모용: DB에 id=1인 유저 있어야 합니다.
    private Long currentUserId() { return 1L; }

    @GetMapping
    public String list(Model model) {
        Long userId = currentUserId();

        var challenges = challengeService.listActive();
        var progresses = challengeService.myProgress(userId);

        Map<Long, ChallengeProgress> progressMap = progresses.stream()
                .collect(Collectors.toMap(p -> p.getChallenge().getId(), Function.identity()));

        long achievedCount = progresses.stream().filter(ChallengeProgress::isAchieved).count();
        long claimableCount = progresses.stream().filter(p -> p.isAchieved() && !p.isClaimed()).count();

        model.addAttribute("challenges", challenges);
        model.addAttribute("progressMap", progressMap);
        model.addAttribute("achievedCount", achievedCount);
        model.addAttribute("claimableCount", claimableCount);
        return "challenges/list";
    }

    @PostMapping("/{challengeId}/claim")
    public String claim(@PathVariable Long challengeId) {
        challengeService.claimReward(currentUserId(), challengeId);
        return "redirect:/challenges";
    }
}
