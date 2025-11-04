package katecam.hyuswim.post.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import jakarta.servlet.http.HttpServletRequest;

import katecam.hyuswim.comment.service.CommentService;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.common.util.WebUtil;
import katecam.hyuswim.like.repository.PostLikeRepository;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;
import katecam.hyuswim.post.dto.*;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.domain.User;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final RedisTemplate<String, String> redisTemplate;
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final PostLikeRepository postLikeRepository;

    private PostDetailResponse buildPostDetailResponse(Post post, User currentUser, long viewCount) {
        long likeCount = post.getPostLikes().stream().filter(l -> !l.getIsDeleted()).count();
        long commentCount = post.getComments().stream().filter(c -> !c.getIsDeleted()).count();

        boolean isAuthor = currentUser != null && post.getUser().getId().equals(currentUser.getId());
        boolean isLiked = currentUser != null &&
                postLikeRepository.existsByUser_IdAndPost_IdAndIsDeletedFalse(currentUser.getId(), post.getId());

        return PostDetailResponse.from(post, isAuthor, isLiked, likeCount, commentCount, viewCount);
    }

    private long increaseViewCount(Long postId, User user, HttpServletRequest request) {
        String ip = WebUtil.getClientIp(request);

        String userIdentifier = (user != null)
                ? "user:" + user.getId()
                : "guest:" + ip;

        String viewKey = "post:viewed:" + postId + ":" + userIdentifier;
        String countKey = "post:viewCount:" + postId;

        Boolean firstView = redisTemplate.opsForValue()
                .setIfAbsent(viewKey, "1", Duration.ofHours(1));

        if (Boolean.TRUE.equals(firstView)) {
            Long newCount = redisTemplate.opsForValue().increment(countKey);
            return newCount != null ? newCount : 0L;
        }

        String current = redisTemplate.opsForValue().get(countKey);
        return current != null ? Long.parseLong(current) : 0L;
    }

    private static PageResponse<PostListResponse> mapPostsWithLiked(
            Page<PostListResponse> postPage,
            User currentUser,
            PostLikeRepository postLikeRepository
    ) {
        List<Long> postIds = postPage.stream()
                .map(PostListResponse::getId)
                .toList();

        Set<Long> likedPostIds = (currentUser == null)
                ? Set.of()
                : new HashSet<>(postLikeRepository.findLikedPostIdsByUserIdAndPostIds(
                currentUser.getId(), postIds
        ));

        var mappedPage = postPage.map(
                post -> post.withLiked(likedPostIds.contains(post.getId()))
        );

        return new PageResponse<>(mappedPage);
    }

    @Transactional
    public PostDetailResponse createPost(PostRequest request, User user) {
        Post post = Post.create(
                request.getTitle(),
                request.getContent(),
                request.getPostCategory(),
                user,
                request.getIsAnonymous()
        );

        Post saved = postRepository.save(post);
        commentService.createAiComment(saved);

        return buildPostDetailResponse(saved, user, saved.getViewCount());
    }

    @Transactional(readOnly = true)
    public PageResponse<PostListResponse> getPosts(Pageable pageable, User currentUser) {
        var postPage = postRepository.findAllSummary(pageable);
        return mapPostsWithLiked(postPage, currentUser, postLikeRepository);
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPost(Long id, User currentUser, HttpServletRequest request) {
        Post post = postRepository.findDetailById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        long redisCount = increaseViewCount(id, currentUser, request);
        long viewCount = post.getViewCount() + redisCount;

        return buildPostDetailResponse(post, currentUser, viewCount);
    }

    @Transactional(readOnly = true)
    public List<PostCategoryResponse> getCategories() {
        return Arrays.stream(PostCategory.values())
                .map(PostCategoryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<PostListResponse> getPostsByCategory(
            PostCategory category, Pageable pageable, User currentUser
    ) {
        var postPage = postRepository.findByCategorySummary(category, pageable);
        return mapPostsWithLiked(postPage, currentUser, postLikeRepository);
    }

    @Transactional(readOnly = true)
    public PageResponse<PostListResponse> searchPosts(
            PostSearchRequest request, Pageable pageable, User currentUser
    ) {
        String keyword = (request.getKeyword() == null || request.getKeyword().isBlank()) ? null : request.getKeyword();

        LocalDateTime start = request.getStartDate() != null ? request.getStartDate().atStartOfDay() : null;
        LocalDateTime end = request.getEndDate() != null
                ? request.getEndDate().plusDays(1).atStartOfDay().minusNanos(1)
                : null;

        var postPage = postRepository.searchSummary(
                keyword, request.getCategory(), start, end, pageable
        );

        return mapPostsWithLiked(postPage, currentUser, postLikeRepository);
    }

    @Transactional
    public PostDetailResponse updatePost(Long id, PostRequest request, User currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }

        post.update(request.getTitle(), request.getContent(), request.getPostCategory());
        return buildPostDetailResponse(post, currentUser, post.getViewCount());
    }

    @Transactional
    public void deletePost(Long id, User currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }

        post.delete();
    }
}
