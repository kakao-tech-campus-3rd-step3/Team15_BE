package katecam.hyuswim.user.dto.mypage;

import java.time.LocalDateTime;

import katecam.hyuswim.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyCommentResponse {

  private Long id;
  private Long postId;
  private String author;
  private String content;
  private Boolean isAnonymous;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static MyCommentResponse from(Comment entity) {
    return MyCommentResponse.builder()
        .id(entity.getId())
        .postId(entity.getPost().getId())
        .author(entity.getUser().getNickname())
        .content(entity.getContent())
        .isAnonymous(entity.getIsAnonymous())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
