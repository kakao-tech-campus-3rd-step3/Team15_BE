package katecam.hyuswim.badge.view;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BadgeCollectionVM {
    private int totalCount;        // 전체 배지 수
    private int earnedCount;       // 획득한 배지
    private int inProgressCount;   // 진행 중 (현재 < threshold 이면서 current>0)
    private int lockedCount;       // 잠김 (current==0)

    private List<BadgeViewItem> earned;     // 탭 1
    private List<BadgeViewItem> inProgress; // 탭 2
    private List<BadgeViewItem> locked;     // 탭 3
    private List<BadgeViewItem> all;        // 탭 4(전체)
}
