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
@Setter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private String password;

  private String nickname = "새싹이";

  private String introduction;

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

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public User(String email, String password, String nickname) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.role = UserRole.USER;
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
}
