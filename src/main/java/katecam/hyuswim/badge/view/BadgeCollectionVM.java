package katecam.hyuswim.badge.view;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class BadgeCollectionVM {

    private int totalCount;
    private int earnedCount;
    private int inProgressCount;
    private int lockedCount;

    private List<BadgeViewItem> earned;
    private List<BadgeViewItem> inProgress;
    private List<BadgeViewItem> locked;
    private List<BadgeViewItem> all;

    public static BadgeCollectionVM of(List<BadgeViewItem> all) {
        var earned = all.stream().filter(BadgeViewItem::isEarned).toList();
        var inProgress = all.stream().filter(v -> !v.isEarned() && v.getCurrent() > 0).toList();
        var locked = all.stream().filter(v -> !v.isEarned() && v.getCurrent() == 0).toList();

        return BadgeCollectionVM.builder()
                .totalCount(all.size())
                .earnedCount(earned.size())
                .inProgressCount(inProgress.size())
                .lockedCount(locked.size())
                .earned(earned)
                .inProgress(inProgress)
                .locked(locked)
                .all(all)
                .build();
    }
}
