package katecam.hyuswim.comment.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findAllByIsDeletedFalse(Pageable pageable);

  Optional<Comment> findByIdAndIsDeletedFalse(Long id);

  List<Comment> findAllByUser_IdAndIsDeletedFalse(Long userId);
}
