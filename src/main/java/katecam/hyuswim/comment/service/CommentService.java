package katecam.hyuswim.comment.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.ai.client.OpenAiClient;
import katecam.hyuswim.ai.service.AiUserService;
import katecam.hyuswim.comment.domain.AuthorTag;
import katecam.hyuswim.comment.domain.AuthorTagResolver;
import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.comment.dto.CommentDetailResponse;
import katecam.hyuswim.comment.dto.CommentListResponse;
import katecam.hyuswim.comment.dto.CommentRequest;
import katecam.hyuswim.comment.dto.CommentTreeResponse;
import katecam.hyuswim.comment.event.CommentCreatedEvent;
import katecam.hyuswim.comment.repository.CommentRepository;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.dto.PageResponse;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthorTagResolver authorTagResolver;
    private final OpenAiClient openAiClient;
    private final AiUserService aiUserService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CommentDetailResponse createComment(User user, Long postId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        AuthorTag authorTag = authorTagResolver.resolve(user, post);

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .authorTag(authorTag)
                .content(request.getContent())
                .isAnonymous(request.getIsAnonymous())
                .build();

        Comment saved = commentRepository.saveAndFlush(comment);
        eventPublisher.publishEvent(new CommentCreatedEvent(user.getId()));

        return CommentDetailResponse.from(saved, true);
    }

    @Transactional
    public void createAiComment(Post post) {
        String aiReply = openAiClient.generateReply(post.getTitle(), post.getContent());
        User aiUser = aiUserService.getAiUser();
        AuthorTag authorTag = authorTagResolver.resolve(aiUser, post);

        Comment aiComment = Comment.builder()
                .user(aiUser)
                .post(post)
                .authorTag(authorTag)
                .content(aiReply)
                .isAnonymous(false)
                .build();

        commentRepository.save(aiComment);
        CommentDetailResponse.from(aiComment, true);
    }

    @Transactional
    public CommentTreeResponse createReplyComment(User user, Long parentId, CommentRequest request) {
        Comment parent = commentRepository.findByIdAndIsDeletedFalse(parentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        Post post = parent.getPost();
        if (post.getIsDeleted()) throw new CustomException(ErrorCode.POST_DELETED);

        AuthorTag authorTag = authorTagResolver.resolve(user, post);

        Comment reply = Comment.builder()
                .user(user)
                .post(post)
                .authorTag(authorTag)
                .content(request.getContent())
                .isAnonymous(request.getIsAnonymous())
                .build();

        reply.assignParent(parent);
        commentRepository.save(reply);

        eventPublisher.publishEvent(new CommentCreatedEvent(user.getId()));
        return CommentTreeResponse.from(reply, user.getId());
    }

    @Transactional(readOnly = true)
    public PageResponse<CommentListResponse> getComments(Long postId, Pageable pageable, User currentUser) {
        Long currentUserId = (currentUser != null) ? currentUser.getId() : null;

        return new PageResponse<>(
                commentRepository.findRootsByPostIdWithUser(postId, pageable)
                        .map(c -> {
                            boolean isAuthor = currentUserId != null && c.getUser().getId().equals(currentUserId);
                            boolean hasChildren = commentRepository.existsByParent_IdAndIsDeletedFalse(c.getId());
                            return CommentListResponse.from(c, isAuthor, hasChildren);
                        })
        );
    }

    @Transactional(readOnly = true)
    public List<CommentTreeResponse> getReplies(Long parentId, User currentUser) {
        Long currentUserId = (currentUser != null) ? currentUser.getId() : null;

        List<Comment> children = commentRepository.findChildrenWithUser(parentId);

        return children.stream()
                .map(child -> CommentTreeResponse.from(child, currentUserId))
                .toList();
    }

    @Transactional(readOnly = true)
    public CommentDetailResponse getComment(Long id, User currentUser) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        boolean isAuthor = currentUser != null && comment.getUser().getId().equals(currentUser.getId());
        return CommentDetailResponse.from(comment, isAuthor);
    }

    @Transactional
    public CommentDetailResponse updateComment(Long id, User user, CommentRequest request) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }

        comment.update(request.getContent());
        return CommentDetailResponse.from(comment, true);
    }

    @Transactional
    public void deleteComment(Long id, User user) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }

        comment.delete();
    }
}
