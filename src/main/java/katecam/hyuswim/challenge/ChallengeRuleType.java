package katecam.hyuswim.challenge;

public enum ChallengeRuleType {
    COUNT_BY_LEVEL("미션 난이도 별 완료 수"),
    COUNT_BY_CATEGORY("카테고리별 완료 수"),
    TOTAL_COMPLETED("전체 완료 수"),
    POINTS_SUM("누적 포인트 합");

    private final String displayName;

    ChallengeRuleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
