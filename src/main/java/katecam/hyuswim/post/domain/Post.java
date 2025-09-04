package katecam.hyuswim.post.domain;

import jakarta.persistence.*;
import katecam.hyuswim.comment.Comment;
import katecam.hyuswim.like.Like;
import katecam.hyuswim.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

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

    public Post(String title, String content, PostCategory postCategory, User user, Boolean isAnonymous) {
        this.title = title;
        this.content = content;
        this.postCategory = postCategory;
        this.user = user;
        this.isAnonymous = isAnonymous;
    }
}

