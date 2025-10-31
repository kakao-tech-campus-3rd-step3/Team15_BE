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
  private boolean isLiked;
  private LocalDateTime createdAt;

  public static PostListResponse from(Post post) {
    return PostListResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .postCategory(post.getPostCategory().name())
        .postCategoryName(post.getPostCategory().getDisplayName())
        .content(post.getContent())
        .author(post.getUser().getDisplayName())
        .likeCount((long) post.getPostLikes().size())
        .commentCount((long) post.getComments().size())
        .viewCount(post.getViewCount())
        .isLiked(false)
        .createdAt(post.getCreatedAt())
        .build();
  }
public static PostListResponse from(Post post, boolean isLiked) {
    return PostListResponse.builder()
            .id(post.getId())
            .title(post.getTitle())
            .postCategory(post.getPostCategory().name())
            .postCategoryName(post.getPostCategory().getDisplayName())
            .content(post.getContent())
            .author(post.getUser().getDisplayName())
            .likeCount((long) post.getPostLikes().size())
            .commentCount((long) post.getComments().size())
            .viewCount(post.getViewCount())
            .isLiked(isLiked)
            .createdAt(post.getCreatedAt())
            .build();
     }
}
