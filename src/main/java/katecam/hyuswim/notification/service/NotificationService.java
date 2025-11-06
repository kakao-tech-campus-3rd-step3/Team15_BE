package katecam.hyuswim.notification.service;

import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.comment.repository.CommentRepository;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.notification.domain.NotiType;
import katecam.hyuswim.notification.domain.Notification;
import katecam.hyuswim.notification.domain.TargetType;
import katecam.hyuswim.notification.event.PostLikedNotificationEvent;
import katecam.hyuswim.notification.event.CommentCreatedNotificationEvent;
import katecam.hyuswim.notification.repository.NotificationRepository;
import katecam.hyuswim.notification.dto.NotificationResponse;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static katecam.hyuswim.common.error.ErrorCode.NOTIFICATION_ACCESS_DENIED;
import static katecam.hyuswim.common.error.ErrorCode.NOTIFICATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createPostLikedNotification(PostLikedNotificationEvent event) {
        User actor = userRepository.findById(event.getActorId())
                .orElseThrow();
        Post post = postRepository.findById(event.getPostId())
                .orElseThrow();
        User receiver = post.getUser();

        if (actor.getId().equals(receiver.getId())) return;

        if (isNotAuthorAction(actor, receiver)) {
            Notification notification = Notification.builder()
                    .typeId(null)
                    .notiType(NotiType.LIKE)
                    .actor(actor)
                    .receiver(receiver)
                    .targetId(post.getId())
                    .targetType(TargetType.POST)
                    .payload("내 게시글에 좋아요가 달렸어요!")
                    .build();

            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void createCommentNotification(CommentCreatedNotificationEvent event) {
        Comment comment = event.getComment();
        Post post = comment.getPost();

        User postAuthor = post.getUser();
        User commentAuthor = comment.getUser();

        if (isNotAuthorAction(commentAuthor, postAuthor)) {
            Notification notification = Notification.builder()
                    .typeId(comment.getId())
                    .notiType(NotiType.COMMENT)
                    .actor(commentAuthor)
                    .receiver(postAuthor)
                    .targetId(post.getId())
                    .targetType(TargetType.POST)
                    .payload("내 게시글에 댓글이 달렸어요!")
                    .build();

            notificationRepository.save(notification);
        }
    }

    private boolean isNotAuthorAction(User actor, User author) {
        return !actor.getId().equals(author.getId());
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(User receiver){
        return notificationRepository.countByReceiverAndIsReadFalse(receiver);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(User receiver) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(receiver.getId())
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional
    public void markAsRead(Long notificationId, User receiver){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(NOTIFICATION_NOT_FOUND));

        if (!notification.getReceiver().getId().equals(receiver.getId())) {
            throw new CustomException(NOTIFICATION_ACCESS_DENIED);
        }

        notification.markAsRead();
    }

    @Transactional
    public void markAllAsRead(User receiver){
        notificationRepository.markAllAsRead(receiver);
    }

    @Transactional
    public void deleteNotification(Long notificationId, User receiver) {
        notificationRepository.deleteByIdAndReceiver(notificationId,receiver);
    }
}

