package katecam.hyuswim.user.controller;

import katecam.hyuswim.user.User;
import katecam.hyuswim.user.dto.JwtTokenRequest;
import katecam.hyuswim.user.dto.JwtTokenResponse;
import katecam.hyuswim.user.dto.LoginRequest;
import katecam.hyuswim.user.dto.SignupRequest;
import katecam.hyuswim.user.exception.UserNotFoundException;
import katecam.hyuswim.user.jwt.JwtUtil;
import katecam.hyuswim.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/api/user/signup")
    public ResponseEntity<String> postSignup(@RequestBody SignupRequest signupRequest) {
        userService.saveUser(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> postLogin(@RequestBody LoginRequest loginRequest) {
        if (!userService.existUser(loginRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        try {
          User loginUser = userService.findUserByEmail(loginRequest.getEmail());
          String token = jwtUtil.generateToken(new JwtTokenRequest(loginRequest.getEmail(), loginUser.getRole()));
          return ResponseEntity.ok(new JwtTokenResponse(token));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 유저를 찾을 수 없습니다.");
        }
    }
}
