package katecam.hyuswim.user.dto.mypage;

import java.time.LocalDateTime;

import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.like.domain.PostLike;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyLikedPostResponse {

  private Long id;
  private Long postId;
  private PostCategory postCategory;
  private String displayName;
  private String postTitle;
  private String postContent;
  private Long likeCount;
  private Long commentCount;
  private Long viewCount;
  private LocalDateTime postCreatedAt;

    public static MyLikedPostResponse from(PostLike entity) {
        Post post = entity.getPost();
        return MyLikedPostResponse.builder()
                .id(entity.getId())
                .postId(post.getId())
                .postCategory(post.getPostCategory())
                .displayName(post.getPostCategory().getDisplayName())
                .postTitle(post.getTitle())
                .postContent(post.getContent())
                .likeCount((long) post.getPostLikes().size())
                .commentCount((long) post.getComments().size())
                .viewCount(post.getViewCount())
                .postCreatedAt(post.getCreatedAt())
                .build();
    }

}
