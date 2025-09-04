package katecam.hyuswim.post.repository;

import katecam.hyuswim.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByIsDeletedFalse();
    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}