package katecam.hyuswim.post.controller;

import katecam.hyuswim.common.ApiResponse;
import katecam.hyuswim.post.dto.PostRequest;
import katecam.hyuswim.post.dto.PostResponse;
import katecam.hyuswim.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<PostResponse> createPost(@RequestBody PostRequest request,
                                                @RequestParam Long userId) {
        PostResponse response = postService.createPost(request, userId);
        return ApiResponse.success(response);
    }
}
