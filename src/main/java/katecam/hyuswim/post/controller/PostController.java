package katecam.hyuswim.post.controller;

import katecam.hyuswim.common.ApiResponse;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.domain.PostCategory;
import katecam.hyuswim.post.dto.PostListResponse;
import katecam.hyuswim.post.dto.PostRequest;
import katecam.hyuswim.post.dto.PostDetailResponse;
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
    public ApiResponse<PostDetailResponse> createPost(@RequestBody PostRequest request,
                                                      @RequestParam Long userId) {
        PostDetailResponse response = postService.createPost(request, userId);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<List<PostListResponse>> getPosts() {
        List<PostListResponse> response = postService.getPosts();
        return ApiResponse.success(response);
    }

    @GetMapping("/category/{category}")
    public ApiResponse<List<PostListResponse>> getPostsByCategory(@PathVariable PostCategory category) {
        return ApiResponse.success(postService.getPostsByCategory(category));
    }

    @GetMapping("/{id}")
    public ApiResponse<PostDetailResponse> getPost(@PathVariable Long id) {
        PostDetailResponse response = postService.getPost(id);
        return ApiResponse.success(response);
    }

    @GetMapping("/search")
    public ApiResponse<List<PostListResponse>> searchPosts(@RequestParam String keyword) {
        return ApiResponse.success(postService.searchPosts(keyword));
    }

    @PatchMapping("/{id}")
    public ApiResponse<PostDetailResponse> updatePost(@PathVariable Long id,
                                                      @RequestBody PostRequest request,
                                                      @RequestParam Long userId) {
        PostDetailResponse response = postService.updatePost(id, request, userId);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePost(@PathVariable Long id,
                                          @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return ApiResponse.success("게시글이 삭제되었습니다.");
    }
}
