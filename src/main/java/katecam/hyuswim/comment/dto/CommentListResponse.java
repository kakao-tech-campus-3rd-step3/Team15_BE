package katecam.hyuswim.comment.dto;

import java.time.LocalDateTime;

import katecam.hyuswim.comment.domain.AuthorTag;
import katecam.hyuswim.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommentListResponse {
  private Long id;
  private Long postId;
  private Long authorId;
  private String author;
  private String handle;
  private AuthorTag authorTag;
  private String content;
  private Boolean isAuthor;
  private Boolean isAnonymous;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean hasChildren;

    public static CommentListResponse from(Comment comment, boolean isAuthor, boolean hasChildren) {
        return CommentListResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .authorId(comment.getUser().getId())
                .author(comment.getUser().getDisplayName())
                .handle(comment.getUser().getHandle())
                .authorTag(comment.getAuthorTag())
                .content(comment.getIsDeleted()
                        ? (hasChildren ? "삭제된 댓글입니다." : null)
                        : comment.getContent())
                .isAuthor(isAuthor)
                .isAnonymous(comment.getIsAnonymous())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .hasChildren(hasChildren)
                .build();
    }
}
