package katecam.hyuswim.user;

import java.time.LocalDateTime;
import java.util.List;

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

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private String password;

  private String nickname;

  private String introduction;

  @Column(name = "profile_image", nullable = false)
  private String profileImage;

  @Column(nullable = false, unique = true)
  private String handle;

  @OneToMany(mappedBy = "user")
  private List<Badge> badges;

  @OneToMany(mappedBy = "user")
  private List<Post> posts;

  @OneToMany(mappedBy = "user")
  private List<Comment> comments;

  @OneToMany(mappedBy = "user")
  private List<MissionProgress> missionProgresses;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private UserStatus status = UserStatus.ACTIVE;

  private LocalDateTime blockedUntil;

  private String blockReason;

  @Column(name = "comment_notification_enabled", nullable = false)
  private Boolean commentNotificationEnabled = true;

  @Column(name = "like_notification_enabled", nullable = false)
  private Boolean likeNotificationEnabled = true;

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

    public void updateProfile(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public String generateHandle() {
        String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int randomIndex = java.util.concurrent.ThreadLocalRandom.current()
                    .nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public void updateProfileImage(String profileImage) {
      this.profileImage = profileImage;
    }

    public void isCommentNotificationEnabled(Boolean enabled) {
        this.commentNotificationEnabled = enabled;
    }
}
