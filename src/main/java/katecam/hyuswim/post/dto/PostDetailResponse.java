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
  private Long viewCount;
  private Long likeCount;
  private Long commentCount;
  private Boolean isAuthor;
  private Boolean isAnonymous;
  private Boolean Liked;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static PostDetailResponse from(Post post) {
    return PostDetailResponse.builder()
        .id(post.getId())
        .postCategory(post.getPostCategory().name())
        .postCategoryName(post.getPostCategory().getDisplayName())
        .title(post.getTitle())
        .content(post.getContent())
        .author(post.getUser().getDisplayName())
        .handle(post.getUser().getHandle())
        .likeCount((long) post.getPostLikes().size())
        .viewCount(post.getViewCount())
        .commentCount((long) post.getComments().size())
        .isAuthor(false)
        .isAnonymous(post.getIsAnonymous())
        .Liked(false)
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }

    public static PostDetailResponse from(Post post,boolean isAuthor, boolean isLiked) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .postCategory(post.getPostCategory().name())
                .postCategoryName(post.getPostCategory().getDisplayName())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getDisplayName())
                .handle(post.getUser().getHandle())
                .likeCount((long) post.getPostLikes().size())
                .viewCount(post.getViewCount())
                .commentCount((long) post.getComments().size())
                .isAuthor(isAuthor)
                .isAnonymous(post.getIsAnonymous())
                .Liked(isLiked)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
