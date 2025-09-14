package katecam.hyuswim.comment.dto;

import java.time.LocalDateTime;

import katecam.hyuswim.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommentDetailResponse {
  private Long id;
  private Long postId;
  private String author;
  private String content;
  private Boolean isAnonymous;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static CommentDetailResponse from(Comment comment) {
    return CommentDetailResponse.builder()
        .id(comment.getId())
        .postId(comment.getPost().getId())
        .author(comment.getUser().getNickname())
        .content(comment.getContent())
        .isAnonymous(comment.getIsAnonymous())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .build();
  }
}
