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
    Page<Post> findByCategoryAndIsDeletedFalse(PostCategory category, Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND (:category IS NULL OR p.category = :category) AND (:keyword IS NULL OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    Page<Post> searchByCategoryAndKeyword(
            @Param("category") PostCategory category,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}