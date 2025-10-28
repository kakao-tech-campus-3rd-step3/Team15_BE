package katecam.hyuswim.like.controller;

import katecam.hyuswim.badge.domain.BadgeKind;
import katecam.hyuswim.badge.service.BadgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.auth.annotation.LoginUser;
import katecam.hyuswim.like.service.PostLikeService;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostLikeController {

  private final PostLikeService postLikeService;
    private final BadgeService badgeService;

  @PostMapping("/posts/{postId}/likes")
  public ResponseEntity<Void> addLike(@PathVariable Long postId, @LoginUser User user) {
    postLikeService.addLike(postId, user);
    badgeService.checkAndGrant(user, BadgeKind.LOVE_EVANGELIST);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/posts/{postId}/likes")
  public ResponseEntity<Void> deleteLike(@PathVariable Long postId, @LoginUser User user) {
    postLikeService.deleteLike(postId, user);
    return ResponseEntity.noContent().build();
  }
}
