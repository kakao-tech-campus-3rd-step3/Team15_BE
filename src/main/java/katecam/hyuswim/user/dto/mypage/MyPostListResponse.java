package katecam.hyuswim.user.dto.mypage;

import java.time.LocalDateTime;

import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyPostListResponse {
  private Long postId;
  private PostCategory category;
  private String displayName;
  private String title;
  private String content;
  private Long likeCount;
  private Long commentCount;
  private Long viewCount;
  private LocalDateTime createdAt;

  public static MyPostListResponse from(Post entity) {
    return MyPostListResponse.builder()
        .postId(entity.getId())
        .category(entity.getPostCategory())
        .displayName(entity.getPostCategory().getDisplayName())
        .title(entity.getTitle())
        .content(entity.getContent())
        .likeCount((long) entity.getPostLikes().size())
        .commentCount((long) entity.getComments().size())
        .viewCount(entity.getViewCount())
        .createdAt(entity.getCreatedAt())
        .build();
  }
}
