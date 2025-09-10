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
  private long todayStartedCount; // 오늘 시작 인원
  private long todayCompletedCount; // 오늘 완료 인원

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

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public Long getPoint() {
    return point;
  }

  public MissionCategory getCategory() {
    return category;
  }

  public MissionLevel getLevel() {
    return level;
  }

  public boolean isActive() {
    return active;
  }

  public long getStartedCount() {
    return todayStartedCount;
  }

  public long getCompletedCount() {
    return todayCompletedCount;
  }

  public TodayState getState() {
    return state;
  }

  public MissionTodayResponse setId(Long id) {
    this.id = id;
    return this;
  }

  public MissionTodayResponse setTitle(String title) {
    this.title = title;
    return this;
  }

  public MissionTodayResponse setContent(String content) {
    this.content = content;
    return this;
  }

  public MissionTodayResponse setPoint(Long point) {
    this.point = point;
    return this;
  }

  public MissionTodayResponse setCategory(MissionCategory category) {
    this.category = category;
    return this;
  }

  public MissionTodayResponse setLevel(MissionLevel level) {
    this.level = level;
    return this;
  }

  public MissionTodayResponse setActive(boolean active) {
    this.active = active;
    return this;
  }

  public MissionTodayResponse setTodayStartedCount(long v) {
    this.todayStartedCount = v;
    return this;
  }

  public MissionTodayResponse setTodayCompletedCount(long v) {
    this.todayCompletedCount = v;
    return this;
  }

  public MissionTodayResponse setState(TodayState state) {
    this.state = state;
    return this;
  }
}
