package katecam.hyuswim.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.comment.domain.Comment;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findByPostIdAndParentIsNull(Long postId, Pageable pageable);

  List<Comment> findByParentIdAndIsDeletedFalse(Long parentId);

  @Query("select case when exists (" +
            "select 1 from Comment c where c.parent.id = :parentId and c.isDeleted = false" +
            ") then true else false end " +
            "from Comment c")
  boolean existsByParentIdAndIsDeletedFalse(Long parentId);

  Optional<Comment> findByIdAndIsDeletedFalse(Long id);

  List<Comment> findAllByUserIdAndIsDeletedFalse(Long userId);
}
