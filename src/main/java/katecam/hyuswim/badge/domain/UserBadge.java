package katecam.hyuswim.badge.domain;

import jakarta.persistence.*;
import katecam.hyuswim.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "user_badges",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_badge_once",
                columnNames = {"user_id", "badge_id"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @CreatedDate
    @Column(name = "earned_at", updatable = false, nullable = false)
    private LocalDateTime earnedAt;

    public UserBadge(User user, Badge badge) {
        this.user = user;
        this.badge = badge;
    }
}

