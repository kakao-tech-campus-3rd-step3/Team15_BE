package katecam.hyuswim.comment.controller;


import katecam.hyuswim.comment.dto.CommentDetailResponse;
import katecam.hyuswim.comment.dto.CommentRequest;
import katecam.hyuswim.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDetailResponse>createComment(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestBody CommentRequest request
            ){
        CommentDetailResponse response = commentService.createComment(request, userId, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
