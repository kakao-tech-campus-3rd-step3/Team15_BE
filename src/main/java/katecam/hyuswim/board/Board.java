package katecam.hyuswim.board;

import jakarta.persistence.*;
import katecam.hyuswim.comment.Comment;
import katecam.hyuswim.like.Like;
import katecam.hyuswim.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Like> likes;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    private String title;

    private String content;

    private Long viewCount;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
