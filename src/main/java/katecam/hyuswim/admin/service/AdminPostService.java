package katecam.hyuswim.admin.service;

import katecam.hyuswim.admin.dto.AdminPostDetailResponse;
import katecam.hyuswim.admin.dto.AdminPostListResponse;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final PostRepository postRepository;

    // 게시글 목록 조회
    public List<AdminPostListResponse> getPosts() {
        return postRepository.findAll().stream()
                .map(AdminPostListResponse::from)
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    public AdminPostDetailResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return AdminPostDetailResponse.from(post);
    }

    // 숨김 처리
    public void hidePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.delete();
        postRepository.save(post);
    }

    // 숨김 해제
    public void unhidePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.unhide();
        postRepository.save(post);
    }

    // 영구 삭제 (DB 삭제)
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        postRepository.delete(post);
    }
}
