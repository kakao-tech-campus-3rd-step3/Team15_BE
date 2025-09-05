package katecam.hyuswim.post.repository;

import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByIsDeletedFalse(Pageable pageable);
    Optional<Post> findByIdAndIsDeletedFalse(Long id);
    List<Post> findByCategoryAndIsDeletedFalse(PostCategory category);
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    List<Post> searchByKeyword(@Param("keyword") String keyword);
}