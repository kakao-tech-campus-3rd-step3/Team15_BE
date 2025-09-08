package katecam.hyuswim.post.dto;

import katecam.hyuswim.post.domain.PostCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {
  private String title;
  private String content;
  private Boolean isAnonymous;
  private PostCategory postCategory;
}
