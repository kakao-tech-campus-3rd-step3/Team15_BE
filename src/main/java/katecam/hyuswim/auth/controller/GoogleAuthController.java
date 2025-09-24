package katecam.hyuswim.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import katecam.hyuswim.auth.dto.AccessTokenResponse;
import katecam.hyuswim.auth.dto.LoginTokens;
import katecam.hyuswim.auth.service.GoogleAuthService;
import katecam.hyuswim.auth.util.CookieUtil;
import katecam.hyuswim.auth.util.RedirectUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/api/auth/google")
public class GoogleAuthController {

    private final CookieUtil cookieUtil;
    private final RedirectUrlBuilder redirectUrlBuilder;
    private final GoogleAuthService googleAuthService;

    @GetMapping("/url")
    public ResponseEntity<String> getGoogleLoginUrl() {
        return ResponseEntity.ok(googleAuthService.generateLoginUrl());
    }


    @GetMapping("/callback")
    public void googleCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        LoginTokens tokens = googleAuthService.loginWithGoogle(code);

        ResponseCookie refreshTokenCookie = cookieUtil.createRefreshTokenCookie(tokens.refreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        String redirectUrl = redirectUrlBuilder.buildGoogleRedirectUrl(tokens.accessToken());
        response.sendRedirect(redirectUrl);
    }
}

