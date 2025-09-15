package katecam.hyuswim.user.controller;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import katecam.hyuswim.auth.jwt.JwtTokenResponse;
import katecam.hyuswim.auth.jwt.JwtUtil;
import katecam.hyuswim.user.dto.LoginRequest;
import katecam.hyuswim.user.dto.SignupRequest;
import katecam.hyuswim.user.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final JwtUtil jwtUtil;

  @PostMapping("/api/user/signup")
  public ResponseEntity<Void> postSignup(@RequestBody SignupRequest signupRequest) {
    userService.saveUser(signupRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/api/auth/login")
  public ResponseEntity<?> postLogin(@RequestBody LoginRequest loginRequest) {
    String jwtToken = userService.login(loginRequest);
    return ResponseEntity.ok(new JwtTokenResponse(jwtToken));
  }

    @DeleteMapping("/api/user")
    public ResponseEntity<Void> deleteUser(@LoginUser User loginUser, @RequestBody Map<String, String> requestMap) {
      userService.deleteUser(loginUser, requestMap.get("confirmText"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
