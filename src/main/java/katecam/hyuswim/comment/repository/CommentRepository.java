package katecam.hyuswim.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findByPostIdAndParentIsNullAndIsDeletedFalse(Long postId, Pageable pageable);

  List<Comment> findByParentIdAndIsDeletedFalse(Long parentId);

  Optional<Comment> findByIdAndIsDeletedFalse(Long id);

  List<Comment> findAllByUserIdAndIsDeletedFalse(Long userId);
}
