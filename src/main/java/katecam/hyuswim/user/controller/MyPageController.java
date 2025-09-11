package katecam.hyuswim.user.controller;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.dto.MyCommentResponse;
import katecam.hyuswim.user.dto.MyLikedPostResponse;
import katecam.hyuswim.user.dto.MyOverviewResponse;
import katecam.hyuswim.user.dto.MyPostListReponse;
import katecam.hyuswim.user.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
