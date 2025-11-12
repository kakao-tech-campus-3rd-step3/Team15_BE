package katecam.hyuswim.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import katecam.hyuswim.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
        select count(c)
        from Comment c
        where c.post.id = :postId
          and c.isDeleted = false
    """)
    int countByPostIdAndIsDeletedFalse(@Param("postId") Long postId);

    @Query(
            value = """
        select c
        from Comment c
        join fetch c.user u
        where c.post.id = :postId
          and c.parent is null
          and (
                c.isDeleted = false
                or (
                    c.isDeleted = true
                    and exists (
                        select 1 from Comment r
                        where r.parent.id = c.id
                          and r.isDeleted = false
                    )
                )
              )
        """,
            countQuery = """
        select count(c)
        from Comment c
        where c.post.id = :postId
          and c.parent is null
          and (
                c.isDeleted = false
                or (
                    c.isDeleted = true
                    and exists (
                        select 1 from Comment r
                        where r.parent.id = c.id
                          and r.isDeleted = false
                    )
                )
              )
        """
    )
    Page<Comment> findRootsByPostIdWithUser(@Param("postId") Long postId, Pageable pageable);

    @Query("""
           select c
           from Comment c
           join fetch c.user u
           where c.parent.id = :parentId
             and c.isDeleted = false
           order by c.id asc
           """)
    List<Comment> findChildrenWithUser(@Param("parentId") Long parentId);

    boolean existsByParent_IdAndIsDeletedFalse(Long parentId);

    Optional<Comment> findByIdAndIsDeletedFalse(Long id);

    List<Comment> findAllByUserIdAndIsDeletedFalse(Long userId);

    @Query("select count(c) from Comment c where c.user.id = :userId and c.isDeleted = false")
    long countActiveByUserId(@Param("userId") Long userId);
}

