package katecam.hyuswim.auth.util;

import katecam.hyuswim.auth.config.CookieProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieUtil {

    private final CookieProperties cookieProperties;

    public CookieUtil(CookieProperties cookieProperties) {
        this.cookieProperties = cookieProperties;
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(cookieProperties.secure())
                .sameSite(cookieProperties.sameSite())
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
    }
}
