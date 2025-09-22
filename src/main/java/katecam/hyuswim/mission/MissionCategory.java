package katecam.hyuswim.mission;

public enum MissionCategory {
    ROUTINE("루틴"),
    ACTIVITY("활동"),
    COMMUNICATION("소통"),
    ETC("기타");

    private final String displayName;

    MissionCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
