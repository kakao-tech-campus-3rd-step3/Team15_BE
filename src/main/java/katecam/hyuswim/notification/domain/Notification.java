package katecam.hyuswim.notification.domain;

import java.time.LocalDateTime;

import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;
import katecam.hyuswim.user.domain.User;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "type_id")
  private Long typeId;

  @Enumerated(EnumType.STRING)
  private NotiType notiType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "actor_id")
  private User actor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", nullable = false)
  private User receiver;

  private Long targetId;

  @Enumerated(EnumType.STRING)
  private TargetType targetType;

  @Builder.Default
  private boolean isRead = false;

  @Lob
  private String payload;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  public void markAsRead(){
        this.isRead = true;
  }
}
