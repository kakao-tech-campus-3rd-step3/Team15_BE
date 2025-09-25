package katecam.hyuswim.notification.controller;

import katecam.hyuswim.notification.dto.NotificationResponse;
import katecam.hyuswim.notification.service.NotificationService;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.auth.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(@LoginUser User loginUser) {
        return ResponseEntity.ok(notificationService.getNotifications(loginUser));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void>markAsRead(@PathVariable Long id, @LoginUser User user){
        notificationService.markAsRead(id, user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/read")
    public ResponseEntity<Void>markAllAsRead(@LoginUser User user){
        notificationService.markAllAsRead(user);
        return ResponseEntity.noContent().build();
    }

}

