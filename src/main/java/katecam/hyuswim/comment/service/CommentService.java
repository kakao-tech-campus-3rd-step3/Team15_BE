package katecam.hyuswim.comment.service;

import com.sun.source.doctree.CommentTree;
import katecam.hyuswim.comment.dto.CommentTreeResponse;
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
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;

  @Transactional
  public CommentDetailResponse createComment(User user, Long postId, CommentRequest request) {
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

    Comment saved = commentRepository.save(comment);

    return CommentDetailResponse.from(saved);
  }

  @Transactional
  public CommentTreeResponse createReplyComment(User user, Long parentId, CommentRequest request){

      Comment parent = commentRepository.findById(parentId)
              .orElseThrow(()-> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

      Post post = parent.getPost();
      if (post.getIsDeleted()) {
          throw new CustomException(ErrorCode.POST_DELETED);
      }
      Comment reply = Comment.builder()
              .user(user)
              .post(post)
              .content(request.getContent())
              .isAnonymous(request.getIsAnonymous())
              .build();

      reply.assignParent(parent);

      commentRepository.save(reply);

      return CommentTreeResponse.from(reply);
  }

  public PageResponse<CommentListResponse> getComments(Long postId, Pageable pageable) {
    return new PageResponse<>(
        commentRepository.findByPostIdAndParentIsNull(postId,pageable).map(comment -> CommentListResponse.from(
                comment,
                commentRepository.existsByParentIdAndIsDeletedFalse(comment.getId())
        ))
    );
  }

  public List<CommentTreeResponse> getReplies(Long parentId){
      List<Comment> children = commentRepository.findByParentIdAndIsDeletedFalse(parentId);
      return children.stream()
              .map(CommentTreeResponse::from)
              .toList();
  }

  public CommentDetailResponse getComment(Long id) {
    Comment comment =
        commentRepository
            .findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    return CommentDetailResponse.from(comment);
  }

  @Transactional
  public CommentDetailResponse updateComment(Long id, User user, CommentRequest request) {
    Comment comment =
        commentRepository
            .findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getUser().getId().equals(user.getId())) {
      throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
    }

    comment.update(request.getContent());
    return CommentDetailResponse.from(comment);
  }

  @Transactional
  public void deleteComment(Long id, User user) {
    Comment comment =
        commentRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getUser().getId().equals(user.getId())) {
      throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
    }

    comment.delete();
  }
}
