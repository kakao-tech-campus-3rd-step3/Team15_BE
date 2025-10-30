package katecam.hyuswim.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeToggleResponse {
    private final boolean liked;
    private final int likeCount;
}

