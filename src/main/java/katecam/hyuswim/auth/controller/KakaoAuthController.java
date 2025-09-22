package katecam.hyuswim.auth.controller;

import katecam.hyuswim.auth.dto.AccessTokenResponse;
import katecam.hyuswim.auth.dto.LoginTokens;
import katecam.hyuswim.auth.service.KakaoAuthService;
import katecam.hyuswim.auth.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/kakao")
public class KakaoAuthController {

    private final CookieUtil cookieUtil;
    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/url")
    public ResponseEntity<String> getKakaoLoginUrl() {
        return ResponseEntity.ok(kakaoAuthService.generateLoginUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<AccessTokenResponse> kakaoCallback(@RequestParam("code") String code) {
        LoginTokens tokens = kakaoAuthService.loginWithKakao(code);

        ResponseCookie refreshTokenCookie = cookieUtil.createRefreshTokenCookie(tokens.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new AccessTokenResponse(tokens.accessToken()));
    }
}
