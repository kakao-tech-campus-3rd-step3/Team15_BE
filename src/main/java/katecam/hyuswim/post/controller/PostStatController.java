package katecam.hyuswim.post.controller;

import katecam.hyuswim.post.dto.PostCountResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import katecam.hyuswim.post.dto.PostStatsResponse;
import katecam.hyuswim.post.service.PostStatService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostStatController {

  private final PostStatService postStatService;

  @GetMapping("/count")
  public ResponseEntity<PostCountResponse> getPostcount(){
      return ResponseEntity.ok(postStatService.getPostCount());
  }

  @GetMapping("/stats")
  public ResponseEntity<PostStatsResponse> getPostStats() {
    return ResponseEntity.ok(postStatService.getPostStats());
  }
}
