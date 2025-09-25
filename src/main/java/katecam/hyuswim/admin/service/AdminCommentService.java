package katecam.hyuswim.admin.service;

import katecam.hyuswim.admin.dto.AdminCommentResponse;
import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.comment.repository.CommentRepository;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommentService {

    private final CommentRepository commentRepository;

    public List<AdminCommentResponse> getComments() {
        return commentRepository.findAll().stream()
                .map(AdminCommentResponse::from)
                .toList();
    }

    public void hideComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        comment.delete();
        commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
