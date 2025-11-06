package katecam.hyuswim.notification.event;

import katecam.hyuswim.comment.domain.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentCreatedNotificationEvent {
    private final Comment comment;
}

