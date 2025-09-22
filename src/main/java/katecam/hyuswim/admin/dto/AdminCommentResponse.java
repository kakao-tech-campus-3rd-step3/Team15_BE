package katecam.hyuswim.admin.dto;

import katecam.hyuswim.comment.domain.Comment;
import java.time.LocalDateTime;

public record AdminCommentResponse(
        Long id,
        String content,
        String writerEmail,
        boolean deleted,
        LocalDateTime createdAt
) {
    public static AdminCommentResponse from(Comment comment) {
        return new AdminCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getEmail(),
                comment.getIsDeleted(),
                comment.getCreatedAt()
        );
    }
}
