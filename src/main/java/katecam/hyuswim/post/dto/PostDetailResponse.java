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
public class PostDetailResponse {
  private Long id;
  private String postCategory;
  private String postCategoryName;
  private String title;
  private String content;
  private String author;
  private Boolean isAnonymous;
  private Boolean isDeleted;
  private Boolean isLiked;
  private Long viewCount;
  private Long likeCount;
  private Long commentCount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static PostDetailResponse from(Post entity) {
    return PostDetailResponse.builder()
        .id(entity.getId())
        .postCategory(entity.getPostCategory().name())
        .postCategoryName(entity.getPostCategory().getDisplayName())
        .title(entity.getTitle())
        .content(entity.getContent())
        .author(entity.getUser().getEmail())
        .isAnonymous(entity.getIsAnonymous())
        .isDeleted(entity.getIsDeleted())
        .isLiked(false)
        .viewCount(entity.getViewCount())
        .likeCount((long) entity.getPostLikes().size())
        .commentCount((long) entity.getComments().size())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
