package katecam.hyuswim.post.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.like.domain.PostLike;
import katecam.hyuswim.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(EnumType.STRING)
  private PostCategory postCategory;

  private String title;

  private String content;

  @Builder.Default
  @Column(name = "is_anonymous")
  private Boolean isAnonymous = false;

  @Builder.Default
  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @Builder.Default private Long viewCount = 0L;

  @Builder.Default
  @OneToMany(mappedBy = "post")
  private List<PostLike> postLikes = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "post")
  private List<Comment> comments = new ArrayList<>();

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public static Post create(
      String title, String content, PostCategory postCategory, User user, Boolean isAnonymous) {

    return Post.builder()
        .title(title)
        .content(content)
        .postCategory(postCategory)
        .user(user)
        .isAnonymous(isAnonymous)
        .build();
  }

  public void increaseViewCount(){
      this.viewCount++;
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
