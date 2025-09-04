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
}

