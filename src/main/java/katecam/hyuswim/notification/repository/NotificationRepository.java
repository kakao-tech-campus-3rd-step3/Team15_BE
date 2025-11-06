package katecam.hyuswim.notification.repository;

import katecam.hyuswim.notification.domain.Notification;
import katecam.hyuswim.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("""
      select n
      from Notification n
      where n.receiver.id = :receiverId
      order by n.createdAt desc
    """)
    List<Notification> findByReceiverIdOrderByCreatedAtDesc(@Param("receiverId") Long receiverId);;


    @Modifying(clearAutomatically = true)
    @Query("update Notification n set n.isRead = true where n.receiver = :receiver and n.isRead = false")
    void markAllAsRead(@Param("receiver") User receiver);

    long countByReceiverAndIsReadFalse(User receiver);

    void deleteByIdAndReceiver(Long id, User receiver);

}
