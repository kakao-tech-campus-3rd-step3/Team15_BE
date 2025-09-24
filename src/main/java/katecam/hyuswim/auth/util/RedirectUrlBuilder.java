package katecam.hyuswim.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedirectUrlBuilder {

    @Value("${frontend.urls}")
    private String[] frontendUrls;

    public String buildGoogleRedirectUrl(String accessToken) {
        return frontendUrls[1] + "/oauth/callback/google#accessToken=" + accessToken;
    }
}
