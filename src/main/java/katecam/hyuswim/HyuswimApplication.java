package katecam.hyuswim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HyuswimApplication {

    public static void main(String[] args) {
        SpringApplication.run(HyuswimApplication.class, args);
    }

}
