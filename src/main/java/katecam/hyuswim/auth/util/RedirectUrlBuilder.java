package katecam.hyuswim.auth.util;

import katecam.hyuswim.common.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedirectUrlBuilder {

    private final AppProperties appProperties;

    public String buildGoogleRedirectUrl(String accessToken) {
        return appProperties.frontendUrlList().get(1) + "/oauth/callback/google#accessToken=" + accessToken;
    }

    public String buildKakaoRedirectUrl(String accessToken) {
        return appProperties.frontendUrlList().get(1) + "/oauth/callback/kakao#accessToken=" + accessToken;
    }
}
