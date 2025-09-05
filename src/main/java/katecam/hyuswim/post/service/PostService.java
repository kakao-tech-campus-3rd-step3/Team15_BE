package katecam.hyuswim.post.service;

import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;
import katecam.hyuswim.post.dto.*;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostDetailResponse createPost(PostRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                request.getPostCategory(),
                user,
                request.getIsAnonymous()
        );

        Post saved = postRepository.save(post);
        return PostDetailResponse.from(saved);
    }

    public PageResponse<PostListResponse> getPosts(Pageable pageable) {
        return new PageResponse<>(
                postRepository.findAllByIsDeletedFalse(pageable)
                        .map(PostListResponse::from)
        );
    }

    public PageResponse<PostListResponse> getPostsByCategory(PostCategory category, Pageable pageable) {
        return new PageResponse<>(
                postRepository.findByCategoryAndIsDeletedFalse(category, pageable)
                        .map(PostListResponse::from)
        );
    }

    public PostDetailResponse getPost(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return PostDetailResponse.from(post);
    }

    public PageResponse<PostListResponse> searchPosts(PostSearchRequest request, Pageable pageable) {
        LocalDateTime startDateTime = (request.getStartDate() != null) ? request.getStartDate().atStartOfDay() : null;
        LocalDateTime endDateTime = (request.getEndDate() != null) ? request.getEndDate().plusDays(1).atStartOfDay().minusNanos(1) : null;

        return new PageResponse<>(
                postRepository.searchByCategoryAndKeywordAndPeriod(
                        request.getCategory(),
                        request.getKeyword(),
                        startDateTime,
                        endDateTime,
                        pageable
                ).map(PostListResponse::from)
        );
    }

    @Transactional
    public PostDetailResponse updatePost(Long id, PostRequest request, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("작성자만 게시글을 수정할 수 있습니다.");
        }

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getPostCategory()
        );

        return PostDetailResponse.from(post);
    }

    @Transactional
    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("작성자만 게시글을 삭제할 수 있습니다.");
        }

        post.delete();
    }
}

