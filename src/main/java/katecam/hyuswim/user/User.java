package katecam.hyuswim.user;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Setter;
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

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, unique = true)
    private String handle;

    private String nickname;

    private String introduction;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

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

    @OneToMany(mappedBy = "user")
    private List<Badge> badges;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<MissionProgress> missionProgresses;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

  public User(String email, String password) {
    this.email = email;
    this.password = password;
    this.nickname = "새싹이";
    this.role = UserRole.USER;
    this.handle = "@"+generateHandle();
    this.profileImage = "default.png";
    this.commentNotificationEnabled = true;
    this.likeNotificationEnabled = true;
  }

  public User(String email, String nickname, String introduction, UserRole role) {
      this.email = email;
      this.password = "N/A";
      this.nickname = nickname;
      this.role = role;
      this.handle = "@ai-" +generateHandle();
      this.profileImage = "AI.png";
      this.introduction = introduction;
      this.status = UserStatus.ACTIVE;
      this.commentNotificationEnabled = true;
      this.likeNotificationEnabled = true;
    }

    public boolean isBlocked() {
        return this.status == UserStatus.BLOCKED;
    }

    public boolean isBanned() {
        return this.status == UserStatus.BANNED;
    }

    public void blockUntil(LocalDateTime until, String reason) {
        this.status = UserStatus.BLOCKED;
        this.blockedUntil = until;
        this.blockReason = reason;
    }

    public void blockPermanently(String reason) {
        this.status = UserStatus.BLOCKED;
        this.blockedUntil = null;
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

    public void updateProfile(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateCommentNotificationEnabled(Boolean enabled) {
        this.commentNotificationEnabled = enabled;
    }

    public void updateLikeNotificationEnabled(Boolean enabled) {
        this.likeNotificationEnabled = enabled;
    }

    private String generateHandle() {
        String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int randomIndex = java.util.concurrent.ThreadLocalRandom.current()
                    .nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
