package katecam.hyuswim.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import katecam.hyuswim.auth.dto.LoginTokens;
import katecam.hyuswim.auth.service.KakaoAuthService;
import katecam.hyuswim.auth.util.CookieUtil;
import katecam.hyuswim.auth.util.RedirectUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/kakao")
public class KakaoAuthController {

    private final CookieUtil cookieUtil;
    private final RedirectUrlBuilder redirectUrlBuilder;
    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/url")
    public ResponseEntity<String> getKakaoLoginUrl() {
        String kakaoUrl = kakaoAuthService.generateLoginUrl();
        return ResponseEntity.ok(kakaoUrl);
    }


    @GetMapping("/callback")
    public void kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        LoginTokens tokens = kakaoAuthService.loginWithKakao(code);

        ResponseCookie refreshTokenCookie = cookieUtil.createRefreshTokenCookie(tokens.refreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        String redirectUrl = redirectUrlBuilder.buildKakaoRedirectUrl(tokens.accessToken());
        response.sendRedirect(redirectUrl);
    }
}
