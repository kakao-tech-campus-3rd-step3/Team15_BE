package katecam.hyuswim.user.dto.mypage;

// Badge, UserBadge는 더 이상 import 하지 않아도 됩니다.
import katecam.hyuswim.badge.domain.BadgeKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class BadgeCollectionResponse {

    private List<EarnedBadge> earnedBadges;
    private List<BadgeInfo> unearnedBadges;
    private List<BadgeInfo> allBadges;


    @Getter
    @AllArgsConstructor
    public static class BadgeInfo {
        private String name;
        private BadgeKind kind;
        private String iconUrl;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class EarnedBadge {
        private String name;
        private BadgeKind kind;
        private String iconUrl; // 필드명 IconUrl -> iconUrl (Java Naming Convention)
        private LocalDateTime earnedAt;
    }
}
