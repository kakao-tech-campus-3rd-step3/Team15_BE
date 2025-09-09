package katecam.hyuswim.like.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.like.domain.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
  Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

  boolean existsByPostIdAndUserId(Long postId, Long uesrId);
}
