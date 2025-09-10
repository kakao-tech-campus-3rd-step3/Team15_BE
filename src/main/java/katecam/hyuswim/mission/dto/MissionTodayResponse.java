// package katecam.hyuswim.mission.dto;
package katecam.hyuswim.mission.dto;

import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.mission.MissionCategory;
import katecam.hyuswim.mission.MissionLevel;

public class MissionTodayResponse {
    private Long id;
    private String title;
    private String content;
    private Long point;
    private MissionCategory category;
    private MissionLevel level;
    private boolean active;

    // 오늘 통계
    private long startedCount;   // 오늘 시작 인원
    private long completedCount; // 오늘 완료 인원

    // 사용자 상태
    private TodayState state;

  public static MissionTodayResponse of(Mission mission) {
    MissionTodayResponse d = new MissionTodayResponse();
    d.id = mission.getId();
    d.title = mission.getTitle();
    d.content = mission.getContent();
    d.point = mission.getPoint();
    d.category = mission.getCategory();
    d.level = mission.getLevel();
    d.active = mission.isActive();
    d.state = TodayState.NOT_STARTED;
    return d;
  }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Long getPoint() { return point; }
    public MissionCategory getCategory() { return category; }
    public MissionLevel getLevel() { return level; }
    public boolean isActive() { return active; }
    public long getStartedCount() { return startedCount; }
    public long getCompletedCount() { return completedCount; }
    public TodayState getState() { return state; }

    public void setStartedCount(long startedCount) { this.startedCount = startedCount; }
    public void setCompletedCount(long completedCount) { this.completedCount = completedCount; }
    public void setState(TodayState state) { this.state = state; }
}
