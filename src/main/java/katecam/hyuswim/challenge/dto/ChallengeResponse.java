package katecam.hyuswim.challenge.dto;

import java.time.LocalDateTime;
import katecam.hyuswim.challenge.ChallengeRuleType;
import katecam.hyuswim.challenge.domain.Challenge;
import katecam.hyuswim.mission.MissionCategory;
import katecam.hyuswim.mission.MissionLevel;

public record ChallengeResponse(
        Long id,
        String code,
        String title,
        String description,
        ChallengeRuleType ruleType,
        long targetValue,
        boolean active,
        long rewardPoints,
        MissionLevel requiredLevel,
        MissionCategory requiredCategory,
        LocalDateTime timeWindowStart,
        LocalDateTime timeWindowEnd,
        String iconUrl
) {
    public static ChallengeResponse of(Challenge c) {
        return new ChallengeResponse(
                c.getId(), c.getCode(), c.getTitle(), c.getDescription(), c.getRuleType(),
                c.getTargetValue(), c.isActive(), c.getRewardPoints(),
                c.getRequiredLevel(), c.getRequiredCategory(),
                c.getTimeWindowStart(), c.getTimeWindowEnd(),
                c.getIconUrl()
        );
    }
}
