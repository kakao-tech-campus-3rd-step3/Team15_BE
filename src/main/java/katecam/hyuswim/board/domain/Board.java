package katecam.hyuswim.board.domain;

import jakarta.persistence.*;
import katecam.hyuswim.comment.Comment;
import katecam.hyuswim.like.Like;
import katecam.hyuswim.user.User;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Like> likes;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

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

    public Board(String title, String content, BoardCategory boardCategory, User user, Boolean isAnonymous) {
        this.title = title;
        this.content = content;
        this.boardCategory = boardCategory;
        this.user = user;
        this.isAnonymous = isAnonymous;
    }
}
