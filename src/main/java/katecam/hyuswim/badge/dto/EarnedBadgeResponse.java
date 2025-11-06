package katecam.hyuswim.badge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.hyuswim.badge.domain.Badge;
import katecam.hyuswim.badge.domain.BadgeKind;
import katecam.hyuswim.badge.domain.BadgeTier;
import katecam.hyuswim.badge.domain.UserBadge;

import java.time.LocalDateTime;

public record EarnedBadgeResponse(
        @JsonProperty("badge_id")
        Long badgeId,
        String name,
        BadgeKind kind,
        BadgeTier tier,
        Integer threshold,
        @JsonProperty("earned_at")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime earnedAt
) {
    public static EarnedBadgeResponse from(UserBadge userBadge) {
        Badge earnedBadge = userBadge.getBadge();
        return new EarnedBadgeResponse(
                earnedBadge.getId(),
                earnedBadge.getName(),
                earnedBadge.getKind(),
                earnedBadge.getTier(),
                earnedBadge.getThreshold(),
                userBadge.getEarnedAt()
        );

    }
}
