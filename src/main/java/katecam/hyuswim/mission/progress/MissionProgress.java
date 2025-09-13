package katecam.hyuswim.mission.progress;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.user.User;
import lombok.Getter;

@Entity
@Table(
    name = "mission_progress",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_progress_user_date",
            columnNames = {"user_id", "progress_date", "mission_id"} // 하루 1회 제약
            ))
@Getter
public class MissionProgress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mission_id")
  private Mission mission;

  @Column(name = "progress_date", nullable = false)
  private LocalDate progressDate;

  @Column(name = "started_at")
  private LocalDateTime startedAt;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @Column(name = "is_completed")
  private Boolean isCompleted = false;

    protected MissionProgress() {}

    private MissionProgress(User user, Mission mission, LocalDate date, LocalDateTime startedAt) {
        this.user = user;
        this.mission = mission;
        this.progressDate = date;
        this.startedAt = startedAt;
        this.isCompleted = false;
    }

    public static MissionProgress startOf(User user, Mission mission, LocalDate date, LocalDateTime now) {
        return new MissionProgress(user, mission, date, now);
    }

    public void complete(LocalDateTime now) {
        if (Boolean.TRUE.equals(this.isCompleted)) return;
        this.isCompleted = true;
        this.completedAt = now;
    }

}
