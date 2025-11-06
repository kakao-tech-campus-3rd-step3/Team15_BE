package katecam.hyuswim.notification.event;

import katecam.hyuswim.like.domain.PostLike;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostLikedEvent {
    private final PostLike postLike;
}
