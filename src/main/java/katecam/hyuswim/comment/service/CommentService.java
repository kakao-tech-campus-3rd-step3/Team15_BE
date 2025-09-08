package katecam.hyuswim.comment.service;

import jakarta.transaction.Transactional;
import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.comment.dto.CommentDetailResponse;
import katecam.hyuswim.comment.dto.CommentListResponse;
import katecam.hyuswim.comment.dto.CommentRequest;
import katecam.hyuswim.comment.repository.CommentRespository;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.dto.PageResponse;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class CommentService {

    private CommentRespository commentRespository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Transactional
    public CommentDetailResponse createComment(CommentRequest request, Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .isAnonymous(request.getIsAnonymous())
                .build();

        Comment saved = commentRespository.save(comment);

        return CommentDetailResponse.from(saved);
    }

    public PageResponse<CommentListResponse> getComments(Pageable pageable){
        return new PageResponse<>(
                commentRespository.findAllByIsDeletedFalse(pageable).map(CommentListResponse::from));
    }

}
