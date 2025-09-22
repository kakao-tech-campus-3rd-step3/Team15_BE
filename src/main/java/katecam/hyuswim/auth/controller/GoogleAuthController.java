package katecam.hyuswim.auth.controller;

import katecam.hyuswim.auth.dto.AccessTokenResponse;
import katecam.hyuswim.auth.dto.LoginTokens;
import katecam.hyuswim.auth.service.GoogleAuthService;
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
@RequestMapping("/api/auth/google")
public class GoogleAuthController {

    private final CookieUtil cookieUtil;
    private final GoogleAuthService googleAuthService;

    @GetMapping("/url")
    public ResponseEntity<String> getGoogleLoginUrl() {
        return ResponseEntity.ok(googleAuthService.generateLoginUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<AccessTokenResponse> googleCallback(@RequestParam("code") String code) {
        LoginTokens tokens = googleAuthService.loginWithGoogle(code);

        ResponseCookie refreshTokenCookie = cookieUtil.createRefreshTokenCookie(tokens.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new AccessTokenResponse(tokens.accessToken()));
    }
}

