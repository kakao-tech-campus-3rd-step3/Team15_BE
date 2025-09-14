package katecam.hyuswim.user.dto.mypage;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyLikedPostResponse {

  private Long likeId;
  private Long postId;
  private String postTitle;
  private String postContent;
  private int postLikeCount;
  private Long postViewCount;
  private LocalDateTime createdAt;
}
