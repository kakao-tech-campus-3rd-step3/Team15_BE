package katecam.hyuswim.mission;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

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

    private String ownerName; // 선택

    @Column(name = "start_at")
    private LocalDate startAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // === getters/setters ===
    public Long getId() { return id; }

    public Long getPoint() { return point; }
    public void setPoint(Long point) { this.point = point; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public MissionCategory getCategory() { return category; }
    public void setCategory(MissionCategory category) { this.category = category; }

    public MissionLevel getLevel() { return level; }
    public void setLevel(MissionLevel level) { this.level = level; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public LocalDate getStartAt() { return startAt; }
    public void setStartAt(LocalDate startAt) { this.startAt = startAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
