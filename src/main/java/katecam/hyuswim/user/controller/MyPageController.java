package katecam.hyuswim.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.dto.mypage.MyCommentResponse;
import katecam.hyuswim.user.dto.mypage.MyLikedPostResponse;
import katecam.hyuswim.user.dto.mypage.MyOverviewResponse;
import katecam.hyuswim.user.dto.mypage.MyPostListReponse;
import katecam.hyuswim.user.service.MyPageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyPageController {

  private final MyPageService myPageService;

  @GetMapping("/api/users/overview")
  public ResponseEntity<MyOverviewResponse> myOverview(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyOverview(loginUser));
  }

  @GetMapping("/api/users/me/posts")
  public ResponseEntity<List<MyPostListReponse>> myPostList(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyPostList(loginUser));
  }

  @GetMapping("/api/users/me/comments")
  public ResponseEntity<List<MyCommentResponse>> myCommentList(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyCommentList(loginUser));
  }

  @GetMapping("/api/users/me/liked-posts")
  public ResponseEntity<List<MyLikedPostResponse>> myLikedPostList(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyLikedPostList(loginUser));
  }
}
