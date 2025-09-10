package katecam.hyuswim.post.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;
import katecam.hyuswim.post.dto.*;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostDetailResponse createPost(PostRequest request, User user) {
        Post post =
                new Post(
                        request.getTitle(),
                        request.getContent(),
                        request.getPostCategory(),
                        user,
                        request.getIsAnonymous());

        Post saved = postRepository.save(post);
        return PostDetailResponse.from(saved);
    }

    public PageResponse<PostListResponse> getPosts(Pageable pageable) {
        return new PageResponse<>(
                postRepository.findAllByIsDeletedFalse(pageable).map(PostListResponse::from));
    }

    public PageResponse<PostListResponse> getPostsByCategory(
            PostCategory category, Pageable pageable) {
        return new PageResponse<>(
                postRepository
                        .findByPostCategoryAndIsDeletedFalse(category, pageable)
                        .map(PostListResponse::from));
    }

    public PostDetailResponse getPost(Long id) {
        Post post =
                postRepository
                        .findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return PostDetailResponse.from(post);
    }

    public PageResponse<PostListResponse> searchPosts(PostSearchRequest request, Pageable pageable) {
        LocalDateTime startDateTime =
                (request.getStartDate() != null) ? request.getStartDate().atStartOfDay() : null;
        LocalDateTime endDateTime =
                (request.getEndDate() != null)
                        ? request.getEndDate().plusDays(1).atStartOfDay().minusNanos(1)
                        : null;

        return new PageResponse<>(
                postRepository
                        .searchByCategoryAndKeywordAndPeriod(
                                request.getCategory(), request.getKeyword(), startDateTime, endDateTime, pageable)
                        .map(PostListResponse::from));
    }

    @Transactional
    public PostDetailResponse updatePost(Long id, PostRequest request, User user) {
        Post post =
                postRepository
                        .findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }

        post.update(request.getTitle(), request.getContent(), request.getPostCategory());

        return PostDetailResponse.from(post);
    }

    @Transactional
    public void deletePost(Long id, User user) {
        Post post =
                postRepository
                        .findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }

        post.delete();
    }
}
