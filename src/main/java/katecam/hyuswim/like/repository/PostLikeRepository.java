package katecam.hyuswim.like.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.like.domain.PostLike;
import org.springframework.data.jpa.repository.Query;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
  Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

    @Query("select count(pl) from PostLike pl where pl.post.id = :postId and pl.isDeleted = false")
    int countByPostId(Long postId);

    int countByUserIdAndIsDeletedFalse(Long userId);

    List<PostLike> findByUserId(Long userId);

}
