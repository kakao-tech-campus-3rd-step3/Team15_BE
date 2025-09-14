package katecam.hyuswim.user.controller;

import java.util.List;

import katecam.hyuswim.user.dto.mypage.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.service.MyPageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MyPageController {

  private final MyPageService myPageService;

  @GetMapping("/overview")
  public ResponseEntity<MyOverviewResponse> myOverview(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyOverview(loginUser));
  }

  @GetMapping("/me/posts")
  public ResponseEntity<List<MyPostListReponse>> myPostList(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyPostList(loginUser));
  }

  @GetMapping("/me/comments")
  public ResponseEntity<List<MyCommentResponse>> myCommentList(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyCommentList(loginUser));
  }

  @GetMapping("/me/liked-posts")
  public ResponseEntity<List<MyLikedPostResponse>> myLikedPostList(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyLikedPostList(loginUser));
  }

  @PatchMapping("/me/profile/edit/info")
  public ResponseEntity<Void> updateMyProfile(@LoginUser User loginUser, @RequestBody ProfileUpdate profileUpdate) {
    myPageService.updateUserProfile(loginUser, profileUpdate);
    return ResponseEntity.ok().build();
  }
}
