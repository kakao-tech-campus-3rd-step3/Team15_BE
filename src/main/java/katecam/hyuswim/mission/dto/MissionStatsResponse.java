package katecam.hyuswim.mission.dto;

public class MissionStatsResponse {
  private long todayStartedCount;
  private long todayCompletedCount;

  public MissionStatsResponse(long todayStartedCount, long todayCompletedCount) {
    this.todayStartedCount = todayStartedCount;
    this.todayCompletedCount = todayCompletedCount;
  }

  public long getTodayStartedCount() {
    return todayStartedCount;
  }

  public long getTodayCompletedCount() {
    return todayCompletedCount;
  }
}
