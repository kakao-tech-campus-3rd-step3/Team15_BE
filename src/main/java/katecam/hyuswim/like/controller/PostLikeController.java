package katecam.hyuswim.like.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.like.service.PostLikeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostLikeController {

  private final PostLikeService postLikeService;

  @PostMapping("/posts/{postId}/likes")
  public ResponseEntity<Void> addLike(@PathVariable Long postId, @RequestParam Long userId) {
    postLikeService.addLike(postId, userId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/posts/{postId}/likes")
  public ResponseEntity<Void> deleteLike(@PathVariable Long postId, @RequestParam Long userId) {
    postLikeService.deleteLike(postId, userId);
    return ResponseEntity.noContent().build();
  }
}
