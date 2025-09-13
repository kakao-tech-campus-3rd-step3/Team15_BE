package katecam.hyuswim.comment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentRequest {
  private final String content;
  private final Boolean isAnonymous;
}
