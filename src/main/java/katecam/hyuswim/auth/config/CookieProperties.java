package katecam.hyuswim.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cookie")
public record CookieProperties (
    boolean secure,
    String sameSite
){}
