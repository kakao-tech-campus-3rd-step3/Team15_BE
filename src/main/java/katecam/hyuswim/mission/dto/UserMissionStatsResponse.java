package katecam.hyuswim.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMissionStatsResponse {
    private long todayStartedCount;
    private long todayCompletedCount;
}

