package katecam.hyuswim.post.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import katecam.hyuswim.comment.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;
import katecam.hyuswim.post.dto.*;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

  private final PostRepository postRepository;
  private final CommentService commentService;

  @Transactional
  public PostDetailResponse createPost(PostRequest request, User user) {
    Post post =
        Post.create(
            request.getTitle(),
            request.getContent(),
            request.getPostCategory(),
            user,
            request.getIsAnonymous());

    Post saved = postRepository.save(post);

    commentService.createAiComment(saved);

    return PostDetailResponse.from(saved);
  }

  public PageResponse<PostListResponse> getPosts(Pageable pageable) {
    return new PageResponse<>(
        postRepository.findAllByIsDeletedFalse(pageable).map(PostListResponse::from));
  }

  public PostDetailResponse getPost(Long id) {
    Post post =
        postRepository
            .findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    return PostDetailResponse.from(post);
  }

  public PageResponse<PostListResponse> searchPosts(PostSearchRequest request, Pageable pageable) {
    String keyword = request.getKeyword();
    LocalDateTime startDateTime = null;
    LocalDateTime endDateTime = null;

    if (keyword == null || keyword.isBlank()) {
      keyword = null;
    }

    if (request.getStartDate() != null) {
      startDateTime = request.getStartDate().atStartOfDay();
    }

    if (request.getEndDate() != null) {
      endDateTime = request.getEndDate().plusDays(1).atStartOfDay().minusNanos(1);
    }

    return new PageResponse<>(
        postRepository
            .searchByCategoryAndKeywordAndPeriod(
                keyword, request.getCategory(), startDateTime, endDateTime, pageable)
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

  public List<PostCategoryResponse> getCategories() {
    return Arrays.stream(PostCategory.values()).map(PostCategoryResponse::from).toList();
  }

  public PageResponse<PostListResponse> getPostsByCategory(
      PostCategory category, Pageable pageable) {
    return new PageResponse<>(
        postRepository
            .findByPostCategoryAndIsDeletedFalse(category, pageable)
            .map(PostListResponse::from));
  }
}
