package katecam.hyuswim.like.controller;

import katecam.hyuswim.auth.annotation.LoginUser;
import katecam.hyuswim.like.dto.LikeToggleResponse;
import katecam.hyuswim.like.service.PostLikeService;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeToggleResponse> toggleLike(
            @PathVariable Long postId,
            @LoginUser User loginUser
    ) {
        LikeToggleResponse response = postLikeService.toggleLike(postId, loginUser);
        return ResponseEntity.ok(response);
    }
}
