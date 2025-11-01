package katecam.hyuswim.comment.controller;

import katecam.hyuswim.auth.annotation.LoginUser;
import katecam.hyuswim.comment.dto.*;
import katecam.hyuswim.comment.service.CommentService;
import katecam.hyuswim.post.dto.PageResponse;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDetailResponse> createComment(
            @PathVariable Long postId,
            @LoginUser User user,
            @RequestBody CommentRequest request) {
        CommentDetailResponse response = commentService.createComment(user, postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/comments/{parentId}/replies")
    public ResponseEntity<CommentTreeResponse> createReplyComment(
            @PathVariable Long parentId,
            @LoginUser User user,
            @RequestBody CommentRequest request) {
        CommentTreeResponse response = commentService.createReplyComment(user, parentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<PageResponse<CommentListResponse>> getComments(
            @PathVariable Long postId,
            @LoginUser User user,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10)
            Pageable pageable) {
        PageResponse<CommentListResponse> response = commentService.getComments(postId, pageable, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comments/{parentId}/replies")
    public ResponseEntity<List<CommentTreeResponse>> getReplies(
            @PathVariable Long parentId,
            @LoginUser User user) {
        List<CommentTreeResponse> response = commentService.getReplies(parentId, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDetailResponse> getComment(
            @PathVariable Long id,
            @LoginUser User user) {
        CommentDetailResponse response = commentService.getComment(id, user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<CommentDetailResponse> updateComment(
            @PathVariable Long id,
            @LoginUser User user,
            @RequestBody CommentRequest request) {
        CommentDetailResponse response = commentService.updateComment(id, user, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @LoginUser User user) {
        commentService.deleteComment(id, user);
        return ResponseEntity.noContent().build();
    }
}
