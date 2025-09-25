package katecam.hyuswim.admin.dto;

import katecam.hyuswim.comment.domain.Comment;
import java.time.LocalDateTime;

public record AdminCommentResponse(
        Long id,
        String content,
        boolean deleted,
        LocalDateTime createdAt
) {
    public static AdminCommentResponse from(Comment comment) {
        return new AdminCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getIsDeleted(),
                comment.getCreatedAt()
        );
    }
}
