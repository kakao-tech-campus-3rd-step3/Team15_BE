package katecam.hyuswim.mission;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long point;

  private String title;

  private String content;

  private boolean active;

  @Enumerated(EnumType.STRING)
  private MissionCategory category;

  @Enumerated(EnumType.STRING)
  private MissionLevel level;

  private String ownerName; // 선택

  @Column(name = "start_at")
  private LocalDate startAt;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

    public boolean isAvailableOn(LocalDate date) {
        return active && (startAt == null || !startAt.isAfter(date));
    }

    public void activate() { this.active = true; }
    public void deactivate() { this.active = false; }

}
