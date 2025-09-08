package katecam.hyuswim.mission.progress;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.user.User;

@Entity
@Table(
        name = "mission_progress",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_progress_user_date",
                columnNames = {"user_id", "progress_date"}   // 하루 1회 제약
        )
)
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

    // === getters/setters ===
    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Mission getMission() { return mission; }
    public void setMission(Mission mission) { this.mission = mission; }

    public LocalDate getProgressDate() { return progressDate; }
    public void setProgressDate(LocalDate progressDate) { this.progressDate = progressDate; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }
}
