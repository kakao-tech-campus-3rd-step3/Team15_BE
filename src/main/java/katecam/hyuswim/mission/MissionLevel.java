package katecam.hyuswim.mission;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

public enum MissionCategory {
    ROUTINE,
    ACTIVITY,
    COMMUNICATION,
    ETC
}

public enum MissionLevel {
    BEGINNER,     // 초급
    INTERMEDIATE, // 중급
    ADVANCED      // 고급
}

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "start_at")
    private LocalDate startAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

