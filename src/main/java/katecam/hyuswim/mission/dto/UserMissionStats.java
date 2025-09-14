package katecam.hyuswim.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMissionStats {
    private long completedCount; // 누적 완료 미션 수
    private long inProgressCount; // 오늘 진행 중(미완료) 수
    private long earnedPoints; // 누적 획득 포인트 합
}
