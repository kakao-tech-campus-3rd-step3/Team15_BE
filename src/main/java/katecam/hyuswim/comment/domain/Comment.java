package katecam.hyuswim.comment.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parent;

  @OneToMany(mappedBy = "parent")
  private List<Comment> children = new ArrayList<>();

  private String content;

  @Column(name = "is_anonymous")
  private Boolean isAnonymous = false;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @Enumerated(EnumType.STRING)
  @Column(name = "author_tag", nullable = false)
  private AuthorTag authorTag;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Builder
  public Comment(User user, Post post, String content, boolean isAnonymous, AuthorTag authorTag) {
    this.user = user;
    this.post = post;
    this.content = content;
    this.isAnonymous = isAnonymous;
    this.authorTag = authorTag;
  }

  public void assignParent(Comment parent){
      this.parent = parent;
      parent.getChildren().add(this);
  }

  public void update(String content) {
    this.content = content;
  }

  public void delete() {
    this.isDeleted = true;
  }
}
