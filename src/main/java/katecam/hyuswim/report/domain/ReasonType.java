package katecam.hyuswim.report.domain;

public enum ReasonType {
    AD("광고"),
    SPAM("도배"),
    ABUSE("욕설/비하"),
    OBSCENE("음란물"),
    PRIVACY("개인정보 침해"),
    COPYRIGHT("저작권 침해"),
    OTHER("기타");

    private final String displayName;

    ReasonType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
