package katecam.hyuswim.challenge.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import katecam.hyuswim.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "challenge_progress",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_challenge_progress_user_challenge",
                columnNames = {"user_id", "challenge_id"}
        ))
@Getter
@NoArgsConstructor
public class ChallengeProgress {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(nullable = false)
    private long currentValue = 0;

    @Column(nullable = false)
    private boolean achieved = false;

    private LocalDateTime achievedAt;

    @Column(nullable = false)
    private boolean claimed = false;   // 보상 수령 여부(선택 기능)

    private LocalDateTime claimedAt;

    public static ChallengeProgress startOf(User user, Challenge challenge) {
        ChallengeProgress cp = new ChallengeProgress();
        cp.user = user;
        cp.challenge = challenge;
        cp.currentValue = 0;
        cp.achieved = false;
        cp.claimed = false;
        return cp;
    }

    public void updateCurrent(long value) {
        this.currentValue = value;
    }

    public void achieve(LocalDateTime now) {
        if (this.achieved) return;
        this.achieved = true;
        this.achievedAt = now;
    }

    public void claim(LocalDateTime now) {
        if (!this.achieved || this.claimed) return;
        this.claimed = true;
        this.claimedAt = now;
    }
}
