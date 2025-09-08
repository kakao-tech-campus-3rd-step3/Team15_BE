package katecam.hyuswim.comment.repository;

import katecam.hyuswim.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

public interface CommentRespository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByIsDeletedFalse(Pageable pageable);
}
