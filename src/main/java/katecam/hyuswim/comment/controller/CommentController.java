package katecam.hyuswim.comment.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.comment.dto.CommentDetailResponse;
import katecam.hyuswim.comment.dto.CommentListResponse;
import katecam.hyuswim.comment.dto.CommentRequest;
import katecam.hyuswim.comment.service.CommentService;
import katecam.hyuswim.post.dto.PageResponse;
import katecam.hyuswim.user.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/posts/{postId}/comments")
  public ResponseEntity<CommentDetailResponse> createComment(
      @PathVariable Long postId, @LoginUser User user, @RequestBody CommentRequest request) {
    CommentDetailResponse response = commentService.createComment(request, user, postId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/posts/{postId}/comments")
  public ResponseEntity<PageResponse<CommentListResponse>> getComments(
      @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10)
          Pageable pageable) {
    return ResponseEntity.ok(commentService.getComments(pageable));
  }

  @GetMapping("/comments/{id}")
  public ResponseEntity<CommentDetailResponse> getComment(@PathVariable Long id) {
    CommentDetailResponse response = commentService.getComment(id);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/comments/{id}")
  public ResponseEntity<CommentDetailResponse> updateComment(
      @PathVariable Long id, @LoginUser User user, @RequestBody CommentRequest request) {
    CommentDetailResponse response = commentService.updateComment(id, user, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/comments/{id}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long id, @LoginUser User user) {
    commentService.deleteComment(id, user);
    return ResponseEntity.noContent().build();
  }
}
