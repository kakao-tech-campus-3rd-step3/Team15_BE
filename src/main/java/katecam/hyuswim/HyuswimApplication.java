package katecam.hyuswim;

import katecam.hyuswim.common.config.AppProperties;
import katecam.hyuswim.auth.config.CookieProperties;
import katecam.hyuswim.auth.config.GoogleProperties;
import katecam.hyuswim.auth.config.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({
        AppProperties.class,
        KakaoProperties.class,
        GoogleProperties.class,
        CookieProperties.class
})
public class HyuswimApplication {

    public static void main(String[] args) {
        SpringApplication.run(HyuswimApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}

