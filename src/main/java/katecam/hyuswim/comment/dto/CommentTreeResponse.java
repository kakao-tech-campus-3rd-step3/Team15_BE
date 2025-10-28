package katecam.hyuswim.comment.dto;

import katecam.hyuswim.comment.domain.AuthorTag;
import katecam.hyuswim.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
public class CommentTreeResponse {
    private Long id;
    private Long postId;
    private String content;
    private Long authorId;
    private String author;
    private String handle;
    private AuthorTag authorTag;
    private Boolean isAuthor;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentTreeResponse> children;

    public static CommentTreeResponse from(Comment comment, Long currentUserId) {
        Long authorId = (comment.getUser() != null) ? comment.getUser().getId() : null;
        boolean isAuthor = (currentUserId != null) && Objects.equals(authorId, currentUserId);

        return CommentTreeResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .authorId(comment.getUser().getId())
                .author(comment.getUser().getDisplayName())
                .handle(comment.getUser().getHandle())
                .authorTag(comment.getAuthorTag())
                .isAuthor(isAuthor)
                .isAnonymous(comment.getIsAnonymous())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .children(
                        comment.getChildren().stream()
                                .map(child -> CommentTreeResponse.from(child, currentUserId))
                                .toList()
                )
                .build();
    }
}
