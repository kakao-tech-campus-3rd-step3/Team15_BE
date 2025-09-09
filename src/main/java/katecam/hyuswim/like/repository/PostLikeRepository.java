package katecam.hyuswim.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.like.domain.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
  boolean existsByPostIdAndUserId(Long postId, Long uesrId);
}
