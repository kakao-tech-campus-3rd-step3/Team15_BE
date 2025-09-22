package katecam.hyuswim;

import katecam.hyuswim.auth.config.AppProperties;
import katecam.hyuswim.auth.config.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({AppProperties.class,KakaoProperties.class})
public class HyuswimApplication {
  public static void main(String[] args) {
    SpringApplication.run(HyuswimApplication.class, args);
  }
}
