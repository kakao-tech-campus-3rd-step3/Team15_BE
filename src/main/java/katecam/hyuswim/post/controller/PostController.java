package katecam.hyuswim.post.controller;

import katecam.hyuswim.post.dto.PostRequest;
import katecam.hyuswim.post.dto.PostResponse;
import katecam.hyuswim.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostResponse createPost(@RequestBody PostRequest request,
                                    @RequestParam Long userId) {
        return postService.createPost(request, userId);
    }
}
