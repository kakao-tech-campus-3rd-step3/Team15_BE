package katecam.hyuswim.notification.listener;

import katecam.hyuswim.notification.event.PostLikedNotificationEvent;
import katecam.hyuswim.notification.event.CommentCreatedNotificationEvent;
import katecam.hyuswim.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleCommentCreated(CommentCreatedNotificationEvent event) {
        notificationService.createCommentNotification(event);
    }

    @EventListener
    public void handlePostLikedNotification(PostLikedNotificationEvent event) {
        notificationService.createPostLikedNotification(event);
    }
}
