package katecam.hyuswim.like.service;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.like.domain.PostLike;
import katecam.hyuswim.like.dto.LikeToggleResponse;
import katecam.hyuswim.like.event.PostLikedEvent;
import katecam.hyuswim.like.repository.PostLikeRepository;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public LikeToggleResponse toggleLike(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        var existing = postLikeRepository.findByPostIdAndUserId(postId, user.getId());
        boolean nowLiked;
        boolean isFirstLike = false;

        if (existing.isPresent()) {
            PostLike like = existing.get();
            like.toggle();
            nowLiked = !like.isDeleted();
        } else {
            postLikeRepository.save(new PostLike(post, user));
            nowLiked = true;
            isFirstLike = true;
        }

        if (isFirstLike) {
            eventPublisher.publishEvent(new PostLikedEvent(user.getId()));
        }

        int likeCount = postLikeRepository.countByPostId(postId);
        return new LikeToggleResponse(nowLiked, likeCount);
    }
}
