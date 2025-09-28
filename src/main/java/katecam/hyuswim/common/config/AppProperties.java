package katecam.hyuswim.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public record AppProperties(String frontendUrls) {

    public List<String> frontendUrlList() {
        return Arrays.asList(frontendUrls.split(","));
    }
}

