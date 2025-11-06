package katecam.hyuswim.notification.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostLikedNotificationEvent {
    private final Long actorId;     // 좋아요 누른 사람
    private final Long postId;      // 좋아요가 눌린 게시글
}
