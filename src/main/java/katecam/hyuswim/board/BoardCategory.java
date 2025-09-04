package katecam.hyuswim.board;

public enum BoardCategory {
    FREE("자유"),
    CAREER("진로취업"),
    STUDY("학업"),
    RELATIONSHIP("대인관계"),
    SOCIAL("사회생활"),
    MENTAL("정신건강"),
    HOBBY("취미/자기계발"),
    FAMILY("가족"),
    TROUBLE ("고민상담");

    private final String displayName;

    BoardCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
