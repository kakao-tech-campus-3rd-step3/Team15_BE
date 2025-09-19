package katecam.hyuswim.badge;

import java.time.LocalDateTime;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import katecam.hyuswim.user.domain.User;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Badge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private BadgeType name;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "icon_url")
  private String iconUrl;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;
}
