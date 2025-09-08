package katecam.hyuswim.comment.repository;

import katecam.hyuswim.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRespository extends JpaRepository<Comment, Long> {
}
