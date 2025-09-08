package katecam.hyuswim.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequest {
    private Long postId;
    private String content;
    private Boolean isAnonymous;
}
