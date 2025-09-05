package katecam.hyuswim.user.controller;

import katecam.hyuswim.common.ApiResponse;
import katecam.hyuswim.user.dto.JwtTokenResponse;
import katecam.hyuswim.user.dto.SignupRequest;
import katecam.hyuswim.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/user/signup")
    public ApiResponse<String> postLogin(@RequestBody SignupRequest signupRequest) {
        userService.saveUser(signupRequest);
        return ApiResponse.success("회원가입 완료");
    }

}
