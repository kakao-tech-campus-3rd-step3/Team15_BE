package katecam.hyuswim.like.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.like.domain.PostLike;
import org.springframework.data.jpa.repository.Query;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
  Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

  boolean existsByPostIdAndUserId(Long postId, Long uesrId);

    @Query("select count(pl) from PostLike pl where pl.user.id = :userId")
    int countByUserId(Long userId);

    List<PostLike> findByUserId(Long userId);

}
