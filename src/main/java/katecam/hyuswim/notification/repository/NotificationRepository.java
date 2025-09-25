package katecam.hyuswim.notification.repository;

import katecam.hyuswim.notification.domain.Notification;
import katecam.hyuswim.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByReceiverOrderByCreatedAtDesc(User receiver);
}
