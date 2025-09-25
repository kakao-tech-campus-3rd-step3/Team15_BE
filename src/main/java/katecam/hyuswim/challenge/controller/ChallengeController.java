package katecam.hyuswim.challenge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.challenge.dto.ChallengeProgressResponse;
import katecam.hyuswim.challenge.dto.ChallengeResponse;
import katecam.hyuswim.challenge.service.ChallengeService;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<List<ChallengeResponse>> listActive() {
        var list = challengeService.listActive().stream().map(ChallengeResponse::of).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/progress")
    public ResponseEntity<List<ChallengeProgressResponse>> myProgress(@LoginUser User loginUser) {
        var list = challengeService.myProgress(loginUser.getId())
                .stream().map(ChallengeProgressResponse::of).toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{challengeId}/claim")
    public ResponseEntity<Void> claim(@LoginUser User loginUser, @PathVariable Long challengeId) {
        challengeService.claimReward(loginUser.getId(), challengeId);
        return ResponseEntity.ok().build();
    }
}
