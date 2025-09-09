package katecam.hyuswim.comment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.comment.dto.CommentDetailResponse;
import katecam.hyuswim.comment.dto.CommentListResponse;
import katecam.hyuswim.comment.dto.CommentRequest;
import katecam.hyuswim.comment.repository.CommentRepository;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.dto.PageResponse;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

  private CommentRepository commentRespository;
  private UserRepository userRepository;
  private PostRepository postRepository;

  @Transactional
  public CommentDetailResponse createComment(CommentRequest request, Long userId, Long postId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    Comment comment =
        Comment.builder()
            .user(user)
            .post(post)
            .content(request.getContent())
            .isAnonymous(request.getIsAnonymous())
            .build();

    Comment saved = commentRespository.save(comment);

    return CommentDetailResponse.from(saved);
  }

  public PageResponse<CommentListResponse> getComments(Pageable pageable) {
    return new PageResponse<>(
        commentRespository.findAllByIsDeletedFalse(pageable).map(CommentListResponse::from));
  }

  public CommentDetailResponse getComment(Long id) {
    Comment comment =
        commentRespository
            .findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    return CommentDetailResponse.from(comment);
  }

  @Transactional
  public CommentDetailResponse updateComment(Long id, Long userId, CommentRequest request) {
    Comment comment =
        commentRespository
            .findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    comment.update(request.getContent());

    if (!comment.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
    }

    return CommentDetailResponse.from(comment);
  }

  @Transactional
  public void deleteComment(Long id, Long userId) {
    Comment comment =
        commentRespository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
    }

    comment.delete();
  }
}
