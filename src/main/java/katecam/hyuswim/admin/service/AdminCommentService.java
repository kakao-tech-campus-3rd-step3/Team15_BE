package katecam.hyuswim.admin.service;

import katecam.hyuswim.admin.dto.AdminCommentResponse;
import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.comment.repository.CommentRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        comment.delete();
        commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
