// package katecam.hyuswim.mission.dto;
package katecam.hyuswim.mission.dto;

import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.mission.MissionCategory;
import katecam.hyuswim.mission.MissionLevel;

public class MissionTodayDto {
    public enum TodayState { NOT_STARTED, IN_PROGRESS, COMPLETED }

    public Long id;
    public String title;
    public String content;
    public Long point;
    public MissionCategory category;
    public MissionLevel level;
    public boolean active;

    // 오늘 통계
    public long startedCount;    // 오늘 시작 인원
    public long completedCount;  // 오늘 완료 인원

    // 사용자 상태
    public TodayState state;

    public static MissionTodayDto of(Mission m) {
        MissionTodayDto d = new MissionTodayDto();
        d.id = m.getId();
        d.title = m.getTitle();
        d.content = m.getContent();
        d.point = m.getPoint();
        d.category = m.getCategory();
        d.level = m.getLevel();
        d.active = m.isActive();
        d.state = TodayState.NOT_STARTED;
        return d;
    }
}
