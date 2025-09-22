package katecam.hyuswim.user.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import katecam.hyuswim.badge.Badge;
import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.mission.progress.MissionProgress;
import katecam.hyuswim.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Column(nullable = false, unique = true)
    private String handle;

    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    private LocalDateTime blockedUntil;

    private String blockReason;

    @Column(name = "comment_notification_enabled", nullable = false)
    private Boolean commentNotificationEnabled = true;

    @Column(name = "like_notification_enabled", nullable = false)
    private Boolean likeNotificationEnabled = true;

    @Column(nullable = false)
    private long points = 0;

    private int score;
    private int level;

    @OneToMany(mappedBy = "user")
    private List<Badge> badges;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<MissionProgress> missionProgresses;

    @Column(name = "last_active_date")
    @CreatedDate
    private LocalDateTime lastActiveDate;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static User createDefault() {
        return new User("새싹이", null, UserRole.USER);
    }

    public User(String nickname, String introduction, UserRole role) {
        this.nickname = nickname;
        this.introduction = introduction;
        this.role = role;
        this.handle = "@" + generateHandle();
    }

    public void blockUntil(LocalDateTime until, String reason) {
        this.status = UserStatus.BLOCKED;
        this.blockedUntil = until;
        this.blockReason = reason;
    }

    public void unblock() {
        this.status = UserStatus.ACTIVE;
        this.blockedUntil = null;
        this.blockReason = null;
    }

    public void ban(String reason) {
        this.status = UserStatus.BANNED;
        this.blockedUntil = null;
        this.blockReason = reason;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public String getDisplayName() {
        return isDeleted ? "탈퇴한 유저" : nickname;
    }

    public void updateProfile(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public void updateCommentNotificationEnabled(Boolean enabled) {
        this.commentNotificationEnabled = enabled;
    }

    public void updateLikeNotificationEnabled(Boolean enabled) {
        this.likeNotificationEnabled = enabled;
    }

    public static String generateHandle() {
        String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int randomIndex = java.util.concurrent.ThreadLocalRandom.current()
                    .nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public void updateLastActiveDate() {
        this.lastActiveDate = LocalDateTime.now();
    }
}
