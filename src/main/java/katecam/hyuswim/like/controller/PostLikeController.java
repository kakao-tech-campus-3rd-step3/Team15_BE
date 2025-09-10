package katecam.hyuswim.like.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.like.service.PostLikeService;
import katecam.hyuswim.user.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostLikeController {

  private final PostLikeService postLikeService;

  @PostMapping("/posts/{postId}/likes")
  public ResponseEntity<Void> addLike(@PathVariable Long postId, @LoginUser User user) {
    postLikeService.addLike(postId, user);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/posts/{postId}/likes")
  public ResponseEntity<Void> deleteLike(@PathVariable Long postId, @LoginUser User user) {
    postLikeService.deleteLike(postId, user);
    return ResponseEntity.noContent().build();
  }
}
