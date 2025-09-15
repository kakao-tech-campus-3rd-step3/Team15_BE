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
  private String handle;
  private Boolean isAnonymous;
  private Boolean isDeleted;
  private Boolean isLiked;
  private Long viewCount;
  private Long likeCount;
  private Long commentCount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static PostDetailResponse from(Post post) {
    return PostDetailResponse.builder()
        .id(post.getId())
        .postCategory(post.getPostCategory().name())
        .postCategoryName(post.getPostCategory().getDisplayName())
        .title(post.getTitle())
        .content(post.getContent())
        .author(post.getUser().getNickname())
        .handle(post.getUser().getHandle())
        .isAnonymous(post.getIsAnonymous())
        .isDeleted(post.getIsDeleted())
        .isLiked(false)
        .viewCount(post.getViewCount())
        .likeCount((long) post.getPostLikes().size())
        .commentCount((long) post.getComments().size())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
