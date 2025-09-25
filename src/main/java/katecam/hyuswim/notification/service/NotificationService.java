package katecam.hyuswim.notification.service;

import katecam.hyuswim.notification.repository.NotificationRepository;
import katecam.hyuswim.notification.dto.NotificationResponse;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(User receiver) {
        return notificationRepository.findByReceiverOrderByCreatedAtDesc(receiver)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }
}

