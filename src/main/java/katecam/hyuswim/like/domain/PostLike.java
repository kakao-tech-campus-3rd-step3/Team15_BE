package katecam.hyuswim.like.domain;

import jakarta.persistence.*;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "post_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"})
)
@Getter
@NoArgsConstructor
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Boolean isDeleted = false;


    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
        this.isDeleted = false;
    }

    public void toggle() {
        this.isDeleted = !this.isDeleted;
    }
}

