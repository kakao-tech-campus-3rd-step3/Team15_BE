package katecam.hyuswim.comment.service;

import katecam.hyuswim.ai.client.OpenAiClient;
import katecam.hyuswim.ai.service.AiUserService;
import katecam.hyuswim.badge.domain.BadgeKind;
import katecam.hyuswim.badge.service.BadgeService;
import katecam.hyuswim.comment.domain.AuthorTag;
import katecam.hyuswim.comment.domain.AuthorTagResolver;
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
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final AuthorTagResolver authorTagResolver;
  private final OpenAiClient openAiClient;
  private final AiUserService aiUserService;
  private final BadgeService badgeService;

  @Transactional
  public CommentDetailResponse createComment(User user, Long postId, CommentRequest request) {
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    AuthorTag authorTag = authorTagResolver.resolve(user,post);

    Comment comment =
        Comment.builder()
            .user(user)
            .post(post)
            .authorTag(authorTag)
            .content(request.getContent())
            .isAnonymous(request.getIsAnonymous())
            .build();

    Comment saved = commentRepository.save(comment);

    badgeService.checkAndGrant(user.getId(), BadgeKind.DILIGENT_COMMENTER);

    return CommentDetailResponse.from(saved);
  }

  @Transactional
  public void createAiComment(Post post){

      String aiReply = openAiClient.generateReply(
              post.getTitle(),
              post.getContent()
      );

      User aiUser = aiUserService.getAiUser();

      AuthorTag authorTag = authorTagResolver.resolve(aiUser,post);

      Comment aiComment =
              Comment.builder()
                      .user(aiUser)
                      .post(post)
                      .authorTag(authorTag)
                      .content(aiReply)
                      .isAnonymous(false)
                      .build();

      commentRepository.save(aiComment);

      CommentDetailResponse.from(aiComment);
  }


  @Transactional
  public CommentTreeResponse createReplyComment(User user, Long parentId, CommentRequest request){

      Comment parent = commentRepository.findById(parentId)
              .orElseThrow(()-> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

      Post post = parent.getPost();
      if (post.getIsDeleted()) {
          throw new CustomException(ErrorCode.POST_DELETED);
      }

      AuthorTag authorTag = authorTagResolver.resolve(user,post);

      Comment reply = Comment.builder()
              .user(user)
              .post(post)
              .authorTag(authorTag)
              .content(request.getContent())
              .isAnonymous(request.getIsAnonymous())
              .build();

      reply.assignParent(parent);

      commentRepository.save(reply);

      badgeService.checkAndGrant(user.getId(), BadgeKind.DILIGENT_COMMENTER);

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
