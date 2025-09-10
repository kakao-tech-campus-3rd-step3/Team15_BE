package katecam.hyuswim.post.dto;

import java.time.LocalDateTime;

import katecam.hyuswim.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListResponse {
  private Long id;
  private String title;
  private String content;
  private String author;
  private Long likeCount;
  private Long viewCount;
  private LocalDateTime createdAt;

  public static PostListResponse from(Post entity) {
    return PostListResponse.builder()
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
