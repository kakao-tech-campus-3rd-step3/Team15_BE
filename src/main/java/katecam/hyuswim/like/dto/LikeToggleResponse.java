package katecam.hyuswim.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeToggleResponse {
    private boolean isLiked;
    private int likeCount;
}


