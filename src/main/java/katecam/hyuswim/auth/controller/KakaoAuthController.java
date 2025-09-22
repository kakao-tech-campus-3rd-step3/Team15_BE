package katecam.hyuswim.auth.controller;

import katecam.hyuswim.auth.dto.AuthResponseDTO;
import katecam.hyuswim.auth.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/api/auth/kakao/url")
    public ResponseEntity<String> getKakaoLoginUrl() {
        return ResponseEntity.ok(kakaoAuthService.generateLoginUrl());
    }

    @GetMapping("/api/auth/kakao/callback")
    public ResponseEntity<AuthResponseDTO> kakaoCallback(@RequestParam("code") String code) {
        return ResponseEntity.ok(kakaoAuthService.loginWithKakao(code));
    }
}
