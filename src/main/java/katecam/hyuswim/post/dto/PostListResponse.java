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
  private String postCategory;
  private String postCategoryName;
  private String title;
  private String content;
  private String author;
  private Long likeCount;
  private Long viewCount;
  private Long commentCount;
  private LocalDateTime createdAt;

  public static PostListResponse from(Post entity) {
    return PostListResponse.builder()
        .id(entity.getId())
        .title(entity.getTitle())
        .postCategory(entity.getPostCategory().name())
        .postCategoryName(entity.getPostCategory().getDisplayName())
        .content(entity.getContent())
        .author(entity.getUser().getDisplayName())
        .likeCount((long) entity.getPostLikes().size())
        .commentCount((long) entity.getComments().size())
        .viewCount(entity.getViewCount())
        .createdAt(entity.getCreatedAt())
        .build();
  }
}
