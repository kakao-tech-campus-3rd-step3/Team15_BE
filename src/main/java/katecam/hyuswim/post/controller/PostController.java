package katecam.hyuswim.post.controller;

import katecam.hyuswim.common.ApiResponse;
import katecam.hyuswim.post.domain.PostCategory;
import katecam.hyuswim.post.dto.PageResponse;
import katecam.hyuswim.post.dto.PostListResponse;
import katecam.hyuswim.post.dto.PostRequest;
import katecam.hyuswim.post.dto.PostDetailResponse;
import katecam.hyuswim.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ApiResponse<PageResponse<PostListResponse>> getPosts(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC,
                    size = 10
            ) Pageable pageable
    ) {
        return ApiResponse.success(postService.getPosts(pageable));
    }

    @GetMapping("/category/{category}")
    public ApiResponse<PageResponse<PostListResponse>> getPostsByCategory(
            @PathVariable PostCategory category,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC,
                    size = 10)
            Pageable pageable
    ) {
        return ApiResponse.success(postService.getPostsByCategory(category, pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<PostDetailResponse> getPost(@PathVariable Long id) {
        PostDetailResponse response = postService.getPost(id);
        return ApiResponse.success(response);
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<PostListResponse>> searchPosts(
            @RequestParam(required = false) PostCategory category,
            @RequestParam(required = false) String keyword,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC,
                    size = 10)
            Pageable pageable
    ) {
        return ApiResponse.success(postService.searchPosts(category, keyword, pageable));
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
