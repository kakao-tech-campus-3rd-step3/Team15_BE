package katecam.hyuswim.notification.service;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.notification.domain.Notification;
import katecam.hyuswim.notification.repository.NotificationRepository;
import katecam.hyuswim.notification.dto.NotificationResponse;
import katecam.hyuswim.user.domain.User;
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

    @Transactional(readOnly = true)
    public long getUnreadCount(User receiver){
        return notificationRepository.countByReceiverAndIsReadFalse(receiver);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(User receiver) {
        return notificationRepository.findByReceiverOrderByCreatedAtDesc(receiver)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional
    public void markAsRead(Long notificationId, User receiver){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(()-> new CustomException(NOTIFICATION_NOT_FOUND));
        if(!notification.getReceiver().equals(receiver)){
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

