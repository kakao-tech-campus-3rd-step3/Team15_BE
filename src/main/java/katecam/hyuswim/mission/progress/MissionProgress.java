package katecam.hyuswim.mission.progress;

import jakarta.persistence.*;
import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.user.User;

import java.time.LocalDateTime;

@Entity
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

    @Column(name = "started_at")
    private LocalDateTime startedAt;  // 시작 시간

    @Column(name = "completed_at")
    private LocalDateTime completedAt;  // 완료 시간

    @Column(name = "is_completed")
    private Boolean isCompleted;  // 완료 여부
}

