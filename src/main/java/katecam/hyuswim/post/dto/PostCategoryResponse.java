package katecam.hyuswim.post.dto;

import katecam.hyuswim.post.domain.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCategoryResponse {
  private String code;
  private String displayName;

  public static PostCategoryResponse from(PostCategory category) {
    return new PostCategoryResponse(category.name(), category.getDisplayName());
  }
}
