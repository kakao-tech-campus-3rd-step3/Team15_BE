package katecam.hyuswim.like.service;

import katecam.hyuswim.badge.domain.BadgeKind;
import katecam.hyuswim.badge.service.BadgeService;
import katecam.hyuswim.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.like.domain.PostLike;
import katecam.hyuswim.like.repository.PostLikeRepository;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeService {

  private final PostLikeRepository postLikeRepository;
  private final PostRepository postRepository;
  private final BadgeService badgeService;

  @Transactional
  public void addLike(Long postId, User user) {
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    postLikeRepository.saveAndFlush(new PostLike(post, user));

      badgeService.checkAndGrant(user.getId(), BadgeKind.LOVE_EVANGELIST);
  }

  @Transactional
  public void deleteLike(Long postId, User user) {
    PostLike postLike =
        postLikeRepository
            .findByPostIdAndUserId(postId, user.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));

    postLikeRepository.delete(postLike);
  }
}
