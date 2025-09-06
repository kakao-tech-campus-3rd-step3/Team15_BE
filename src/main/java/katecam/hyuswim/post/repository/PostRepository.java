package katecam.hyuswim.post.repository;

import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Post> findByIdAndIsDeletedFalse(Long id);

    Page<Post> findByPostCategoryAndIsDeletedFalse(PostCategory postCategory, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.isDeleted = false " +
            "AND (:category IS NULL OR p.postCategory = :category) " +
            "AND (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:startDate IS NULL OR p.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR p.createdAt <= :endDate)")
    Page<Post> searchByCategoryAndKeywordAndPeriod(
            @Param("category") PostCategory category,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    long countByIsDeletedFalse();

    long countByIsDeletedFalseAndCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}