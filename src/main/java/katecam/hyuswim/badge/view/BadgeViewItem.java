package katecam.hyuswim.badge.view;

import katecam.hyuswim.badge.domain.BadgeKind;
import katecam.hyuswim.badge.domain.BadgeTier;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BadgeViewItem {
    private Long badgeId;
    private BadgeKind kind;
    private BadgeTier tier;
    private String name;

    private boolean earned;               // 획득 여부
    private LocalDateTime earnedAt;       // 획득일(획득한 경우)

    private int threshold;                // 1/7/15/30
    private int current;                  // 현재 누적치(좋아요/댓글/일수)
    private int percent;                  // 진행률 (0~100)
}
