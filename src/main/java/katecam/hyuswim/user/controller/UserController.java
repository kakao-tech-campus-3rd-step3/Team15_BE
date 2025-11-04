package katecam.hyuswim.user.controller;

import katecam.hyuswim.auth.annotation.LoginUser;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.dto.UserSummaryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import katecam.hyuswim.user.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@LoginUser User loginUser, @RequestBody Map<String, String> requestMap) {
      userService.deleteUser(loginUser, requestMap.get("confirmText"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/me/summary")
    public UserSummaryResponse getMyNameAndHandle(@LoginUser User currentUser) {
        return userService.getNameAndHandle(currentUser);
    }
}
