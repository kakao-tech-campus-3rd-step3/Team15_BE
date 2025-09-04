package katecam.hyuswim.post.service;

import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.post.dto.PostRequest;
import katecam.hyuswim.post.dto.PostResponse;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse createPost(PostRequest request, Long userId) {
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
        return PostResponse.from(saved);
    }

    public List<PostResponse> getPosts() {
        return postRepository.findAllByIsDeletedFalse().stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse updatePost(Long id, PostRequest request, Long userId) {
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

        return PostResponse.from(post);
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

