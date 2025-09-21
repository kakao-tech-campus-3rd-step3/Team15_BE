package katecam.hyuswim.user.dto.mypage;

import java.time.LocalDateTime;

import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyCommentResponse {

  private Long id;
  private String content;
  private Long postId;
  private PostCategory postCategory;
  private String displayName;
  private String postTitle;
  private String postContent;
  private Boolean isAnonymous;
  private Long likeCount;
  private Long commentCount;
  private Long viewCount;
  private LocalDateTime createdAt;

  public static MyCommentResponse from(Comment entity) {
      Post post = entity.getPost();
    return MyCommentResponse.builder()
        .id(entity.getId())
        .content(entity.getContent())
        .postId(post.getId())
        .postCategory(post.getPostCategory())
        .displayName(post.getPostCategory().getDisplayName())
        .postTitle(post.getTitle())
        .postContent(post.getContent())
        .isAnonymous(entity.getIsAnonymous())
        .likeCount((long) post.getPostLikes().size())
        .commentCount((long) post.getComments().size())
        .viewCount(post.getViewCount())
        .createdAt(entity.getCreatedAt())
        .build();
  }
}
