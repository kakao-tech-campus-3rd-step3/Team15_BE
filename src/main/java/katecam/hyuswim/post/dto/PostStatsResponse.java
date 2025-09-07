package katecam.hyuswim.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostStatsResponse {
    private long totalCount;
    private long weekCount;
    private long todayCount;
}

