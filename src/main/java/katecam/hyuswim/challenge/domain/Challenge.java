package katecam.hyuswim.challenge.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import katecam.hyuswim.challenge.ChallengeRuleType;
import katecam.hyuswim.mission.MissionCategory;
import katecam.hyuswim.mission.MissionLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "challenge", indexes = {
        @Index(name = "idx_challenge_active", columnList = "active")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_challenge_code", columnNames = "code")
})
@Getter
@NoArgsConstructor
public class Challenge {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String code; // ex) STEP_BY_STEP, KING_OF_ACTIVITY

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ChallengeRuleType ruleType;

    @Column(nullable = false)
    private long targetValue;   // 달성에 필요한 값(개수/포인트 등)

    @Column(nullable = false)
    private boolean active = true;

    // 보상
    @Column(nullable = false)
    private long rewardPoints = 0;

    // 필터(옵션)
    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private MissionLevel requiredLevel;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private MissionCategory requiredCategory;

    private LocalDateTime timeWindowStart;
    private LocalDateTime timeWindowEnd;

    private String iconUrl;

    public Challenge(String code, String title, String description,
                     ChallengeRuleType ruleType, long targetValue, long rewardPoints,
                     MissionLevel requiredLevel, MissionCategory requiredCategory,
                     LocalDateTime timeWindowStart, LocalDateTime timeWindowEnd, String iconUrl,
                     boolean active) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.ruleType = ruleType;
        this.targetValue = targetValue;
        this.rewardPoints = rewardPoints;
        this.requiredLevel = requiredLevel;
        this.requiredCategory = requiredCategory;
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
        this.iconUrl = iconUrl;
        this.active = active;
    }
}
