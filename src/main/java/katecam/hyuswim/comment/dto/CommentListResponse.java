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
  private String author;
  private String handle;
  private AuthorTag authorTag;
  private String content;
  private Boolean isAnonymous;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean hasChildren;

    public static CommentListResponse from(Comment comment, boolean hasChildren) {
        return CommentListResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .author(comment.getUser().getNickname())
                .handle(comment.getUser().getHandle())
                .authorTag(comment.getAuthorTag())
                .content(comment.getIsDeleted()
                        ? (hasChildren ? "삭제된 댓글입니다." : null)
                        : comment.getContent())
                .isAnonymous(comment.getIsAnonymous())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .hasChildren(hasChildren)
                .build();
    }
}
