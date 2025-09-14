package katecam.hyuswim.post.domain;

public enum PostCategory {
    FREE("자유"),
    STUDY("학업"),
    CAREER("진로취업"),
    RELATIONSHIP("대인관계"),
    SOCIAL("사회생활"),
    FAMILY("가족"),
    HOBBY("취미"),
    MENTAL("정신건강"),
    TROUBLE("고민상담");

  private final String displayName;

  PostCategory(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
