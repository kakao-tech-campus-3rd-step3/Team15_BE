package katecam.hyuswim.notification.dto;

import katecam.hyuswim.notification.domain.NotiType;
import katecam.hyuswim.notification.domain.Notification;
import katecam.hyuswim.notification.domain.TargetType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private Long typeId;
    private NotiType type;
    private Long actorId;
    private String actorNickname;
    private Long receiverId;
    private Long targetId;
    private TargetType targetType;
    private String payload;
    private boolean isRead;
    private LocalDateTime createdAt;


    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .typeId(notification.getTypeId())
                .type(notification.getNotiType())
                .actorId(notification.getActor() != null ? notification.getActor().getId() : null)
                .actorNickname(notification.getActor() != null ? notification.getActor().getNickname() : null)
                .receiverId(notification.getReceiver().getId())
                .targetId(notification.getTargetId())
                .targetType(notification.getTargetType())
                .payload(notification.getPayload())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}

