package katecam.hyuswim.user;

import jakarta.persistence.*;
import katecam.hyuswim.badge.Badge;
import katecam.hyuswim.board.Board;
import katecam.hyuswim.comment.Comment;
import katecam.hyuswim.mission.progress.MissionProgress;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    @OneToMany(mappedBy = "user")
    private List<Badge> badges;

    @OneToMany(mappedBy = "user")
    private List<Board> boards;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<MissionProgress> missionProgresses;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
