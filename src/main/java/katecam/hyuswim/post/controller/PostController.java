package katecam.hyuswim.post.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.post.domain.PostCategory;
import katecam.hyuswim.post.dto.*;
import katecam.hyuswim.post.service.PostService;
import katecam.hyuswim.user.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping
  public ResponseEntity<PostDetailResponse> createPost(
      @RequestBody PostRequest request, @LoginUser User user) {
    PostDetailResponse response = postService.createPost(request, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<PageResponse<PostListResponse>> getPosts(
      @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10)
          Pageable pageable) {
    return ResponseEntity.ok(postService.getPosts(pageable));
  }

  @GetMapping("/category/{category}")
  public ResponseEntity<PageResponse<PostListResponse>> getPostsByCategory(
      @PathVariable PostCategory category,
      @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10)
          Pageable pageable) {
    return ResponseEntity.ok(postService.getPostsByCategory(category, pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long id) {
    PostDetailResponse response = postService.getPost(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  public ResponseEntity<PageResponse<PostListResponse>> searchPosts(
      PostSearchRequest request,
      @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10)
          Pageable pageable) {
    return ResponseEntity.ok(postService.searchPosts(request, pageable));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<PostDetailResponse> updatePost(
      @PathVariable Long id, @RequestBody PostRequest request, @LoginUser User user) {
    PostDetailResponse response = postService.updatePost(id, request, user);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable Long id, @LoginUser User user) {
    postService.deletePost(id, user);
    return ResponseEntity.noContent().build();
  }
}
