package katecam.hyuswim.like;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.user.User;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Like {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "board_id")
  private Post post;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;
}
