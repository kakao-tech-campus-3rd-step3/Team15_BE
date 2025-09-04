package katecam.hyuswim.post.repository;

import katecam.hyuswim.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}