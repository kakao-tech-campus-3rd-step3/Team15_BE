package katecam.hyuswim.badge.dto;

import katecam.hyuswim.badge.domain.BadgeKind;
import katecam.hyuswim.badge.domain.UserBadge;

import java.time.LocalDateTime;

public record EarnedBadgeResponse(
        Long badgeId,
        String name,
        BadgeKind kind,
        String tier,
        Integer threshold,
        LocalDateTime earnedAt
) {
    public static EarnedBadgeResponse from(UserBadge ub) {
        var b = ub.getBadge();
        return new EarnedBadgeResponse(
                b.getId(),
                b.getName(),
                b.getKind(),
                b.getTier().name(),
                b.getThreshold(),
                ub.getEarnedAt()
        );
    }
}
