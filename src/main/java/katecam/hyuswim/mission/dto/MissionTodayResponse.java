package katecam.hyuswim.mission.dto;

import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.mission.MissionCategory;
import katecam.hyuswim.mission.MissionLevel;
import katecam.hyuswim.mission.TodayState;
import lombok.Getter;

@Getter
public class MissionTodayResponse {
  private Long id;
  private String title;
  private String content;
  private Long point;
  private MissionCategory category;
  private MissionLevel level;
  private boolean active;

  // 오늘 통계
  private long todayStartedCount; // 오늘 시작 인원
  private long todayCompletedCount; // 오늘 완료 인원

  // 사용자 상태
  private TodayState state;

    private MissionTodayResponse(
            Long id, String title, String content, Long point,
            MissionCategory category, MissionLevel level, boolean active,
            long todayStartedCount, long todayCompletedCount, TodayState state) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.point = point;
        this.category = category;
        this.level = level;
        this.active = active;
        this.todayStartedCount = todayStartedCount;
        this.todayCompletedCount = todayCompletedCount;
        this.state = state;
    }

    public static MissionTodayResponse of(Mission m, long started, long completed, TodayState state) {
        return new MissionTodayResponse(
                m.getId(), m.getTitle(), m.getContent(), m.getPoint(),
                m.getCategory(), m.getLevel(), m.isActive(),
                started, completed, state
        );
  }
}
