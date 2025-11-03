package katecam.hyuswim.post.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import katecam.hyuswim.post.dto.PostListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
    select new katecam.hyuswim.post.dto.PostListResponse(
        p.id,
        p.postCategory,
        p.title,
        p.content,
        u.nickname,
        count(distinct pl.id),
        count(distinct c.id),
        p.viewCount,
        p.createdAt
    )
    from Post p
    join p.user u
    left join p.postLikes pl on pl.isDeleted = false
    left join p.comments c on c.isDeleted = false
    where p.isDeleted = false
    group by p.id, p.postCategory, p.title, p.content, u.nickname, p.viewCount, p.createdAt
    order by p.createdAt desc
    """)
    Page<PostListResponse> findAllSummary(Pageable pageable);

    @Query("""
    select new katecam.hyuswim.post.dto.PostListResponse(
        p.id,
        p.postCategory,
        p.title,
        p.content,
        u.nickname,
        count(distinct pl.id),
        count(distinct c.id),
        p.viewCount,
        p.createdAt
    )
    from Post p
    join p.user u
    left join p.postLikes pl on pl.isDeleted = false
    left join p.comments c on c.isDeleted = false
    where p.isDeleted = false
      and p.postCategory = :category
    group by p.id, p.postCategory, p.title, p.content, u.nickname, p.viewCount, p.createdAt
    order by p.createdAt desc
    """)
    Page<PostListResponse> findByCategorySummary(
            @Param("category") PostCategory category,
            Pageable pageable
    );

    @Query("""
    select new katecam.hyuswim.post.dto.PostListResponse(
        p.id,
        p.postCategory,
        p.title,
        p.content,
        u.nickname,
        count(distinct pl.id),
        count(distinct c.id),
        p.viewCount,
        p.createdAt
    )
    from Post p
    join p.user u
    left join p.postLikes pl on pl.isDeleted = false
    left join p.comments c on c.isDeleted = false
    where p.isDeleted = false
      and (:category is null or p.postCategory = :category)
      and (:keyword is null
           or p.title like concat('%', :keyword, '%')
           or p.content like concat('%', :keyword, '%'))
      and (:startDate is null or p.createdAt >= :startDate)
      and (:endDate is null or p.createdAt <= :endDate)
    group by p.id, p.postCategory, p.title, p.content, u.nickname, p.viewCount, p.createdAt
    order by p.createdAt desc
    """)
    Page<PostListResponse> searchSummary(
            @Param("keyword") String keyword,
            @Param("category") PostCategory category,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("""
    select distinct p from Post p
    join fetch p.user u
    left join p.postLikes pl on pl.isDeleted = false
    left join p.comments c on c.isDeleted = false
    where p.id = :id and p.isDeleted = false
""")
    Optional<Post> findDetailById(@Param("id") Long id);

    long countByIsDeletedFalse();

    long countByIsDeletedFalseAndCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Post> findAllByUser_IdAndIsDeletedFalse(Long userId);
}
