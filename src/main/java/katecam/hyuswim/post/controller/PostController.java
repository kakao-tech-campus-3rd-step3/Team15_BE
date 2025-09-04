package katecam.hyuswim.post.controller;

import katecam.hyuswim.common.ApiResponse;
import katecam.hyuswim.post.dto.PostRequest;
import katecam.hyuswim.post.dto.PostResponse;
import katecam.hyuswim.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts() {
        List<PostResponse> response = postService.getPosts();
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long id) {
        PostResponse response = postService.getPost(id);
        return ApiResponse.success(response);
    }

    @PatchMapping("/{id}")
    public ApiResponse<PostResponse> updatePost(@PathVariable Long id,
                                                @RequestBody PostRequest request,
                                                @RequestParam Long userId) {
        PostResponse response = postService.updatePost(id, request, userId);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePost(@PathVariable Long id,
                                          @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return ApiResponse.success("게시글이 삭제되었습니다.");
    }
}
