package katecam.hyuswim.user.controller;

import java.util.List;
import java.util.Map;

import katecam.hyuswim.auth.dto.EmailSendRequest;
import katecam.hyuswim.auth.dto.EmailVerifyRequest;
import katecam.hyuswim.auth.service.AuthEmailService;
import katecam.hyuswim.user.dto.mypage.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.auth.annotation.LoginUser;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.service.MyPageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MyPageController {

  private final MyPageService myPageService;
  private final AuthEmailService authEmailService;

  @GetMapping("/me")
  public ResponseEntity<MyOverviewResponse> myOverview(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyOverview(loginUser));
  }

  @GetMapping("/me/posts")
  public ResponseEntity<List<MyPostListResponse>> myPostList(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyPostList(loginUser));
  }

  @GetMapping("/me/comments")
  public ResponseEntity<List<MyCommentResponse>> myCommentList(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyCommentList(loginUser));
  }

  @GetMapping("/me/likes")
  public ResponseEntity<List<MyLikedPostResponse>> myLikedPostList(@LoginUser User loginUser) {
    return ResponseEntity.ok(myPageService.selectMyLikedPostList(loginUser));
  }

  @GetMapping("/me/profile/edit")
  public ResponseEntity<MyProfileEditResponse> selectMyProfileEdit(@LoginUser User loginUser) {
      return ResponseEntity.ok(myPageService.selectMyProfileEdit(loginUser));
  }

  @PatchMapping("/me/profile/edit/info")
  public ResponseEntity<Void> updateMyProfile(@LoginUser User loginUser, @RequestBody ProfileUpdate profileUpdate) {
    myPageService.updateUserProfile(loginUser, profileUpdate);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/me/notification/comment")
    public ResponseEntity<Void> updateCommentNotification(@LoginUser User loginUser, @RequestBody Map<String, Boolean> requestMap) {
      myPageService.updateCommentNotification(loginUser, requestMap.get("enabled"));
      return ResponseEntity.ok().build();
  }

  @PutMapping("/me/notification/like")
    public ResponseEntity<Void> updateLikeNotification(@LoginUser User loginUser, @RequestBody Map<String, Boolean> requestMap) {
      myPageService.updateLikeNotification(loginUser, requestMap.get("enabled"));
      return ResponseEntity.ok().build();
  }

  @GetMapping("/me/profile")
    public ResponseEntity<MyProfileReponse> myProfile(@LoginUser User loginUser) {
      return ResponseEntity.ok(myPageService.selectMyProfile(loginUser));
  }

  @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@LoginUser User loginUser, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
      myPageService.updatePassword(loginUser, passwordUpdateRequest);
      return ResponseEntity.ok().build();
  }

  @GetMapping("/me/email")
    public ResponseEntity<Map<String,String>> myEmail(@LoginUser User loginUser) {
      return ResponseEntity.ok(myPageService.selectEmail(loginUser));
  }

    @PostMapping("/me/email/send")
    public ResponseEntity<Void> sendEmailCode(
            @RequestBody EmailSendRequest emailSendRequest
    ) {
        authEmailService.sendCode(emailSendRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/email")
    public ResponseEntity<Void> updateEmail(@LoginUser User loginUser, @RequestBody EmailVerifyRequest emailVerifyRequest) {
      myPageService.verifyEmailCode(emailVerifyRequest);
      myPageService.updateEmail(loginUser, emailVerifyRequest.getEmail());
      return ResponseEntity.ok().build();
  }

  @GetMapping("/me/badges")
    public ResponseEntity<BadgeCollectionResponse> selectBadgeCollection(@LoginUser User loginUser) {
      return ResponseEntity.ok(myPageService.selectBadgeCollection(loginUser));
  }


}
