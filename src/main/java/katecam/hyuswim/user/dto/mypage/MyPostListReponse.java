package katecam.hyuswim.user.dto.mypage;

import java.time.LocalDateTime;

import katecam.hyuswim.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyPostListReponse {
  private Long id;
  private String title;
  private String content;
  private String author;
  private Long likeCount;
  private Long viewCount;
  private LocalDateTime createdAt;

  public static MyPostListReponse from(Post entity) {
    return MyPostListReponse.builder()
        .id(entity.getId())
        .title(entity.getTitle())
        .content(entity.getContent())
        .author(entity.getUser().getEmail())
        .likeCount((long) entity.getPostLikes().size())
        .viewCount(entity.getViewCount())
        .createdAt(entity.getCreatedAt())
        .build();
  }
}
