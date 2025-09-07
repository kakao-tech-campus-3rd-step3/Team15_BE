package katecam.hyuswim.post.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import katecam.hyuswim.comment.Comment;
import katecam.hyuswim.like.Like;
import katecam.hyuswim.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "post")
  private List<Like> likes;

  @OneToMany(mappedBy = "post")
  private List<Comment> comments;

  @Enumerated(EnumType.STRING)
  private PostCategory postCategory;

  private String title;
  private String content;

  private Long viewCount = 0L;

  @Column(name = "is_anonymous")
  private Boolean isAnonymous = false;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public Post(
      String title, String content, PostCategory postCategory, User user, Boolean isAnonymous) {
    this.title = title;
    this.content = content;
    this.postCategory = postCategory;
    this.user = user;
    this.isAnonymous = isAnonymous;
  }

  public void update(String title, String content, PostCategory postCategory) {
    if (title != null) this.title = title;
    if (content != null) this.content = content;
    if (postCategory != null) this.postCategory = postCategory;
  }

  public void delete() {
    this.isDeleted = true;
  }
}
