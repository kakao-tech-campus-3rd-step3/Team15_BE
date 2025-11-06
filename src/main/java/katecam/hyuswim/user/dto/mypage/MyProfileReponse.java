package katecam.hyuswim.user.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MyProfileReponse {

    private UserInfo user;
    private UserStats stats;
    private List<BadgeInfo> badges;
    private List<PostSummary> posts;
    private List<CommentInfo> comments;
    private List<LikedPostInfo> likePosts;
    private AccountInfo account;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserInfo {
        private String nickname;
        private LocalDateTime joinDate;
        private LocalDateTime lastActiveDate;
        private Integer score;
        private Integer level;
        private long points;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserStats {
        private Integer totalPosts;
        private Integer totalComments;
        private Integer totalLikes;
        private Integer totalMissionClear;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BadgeInfo {
        private String name;
        private String iconUrl;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PostSummary {
        private Long postId;
        private String title;
        private Integer likeCount;
        private Integer commentCount;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class CommentInfo {
        private Long commentId;
        private String content;
        private Long postId;
        private String postTitle;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class LikedPostInfo {
        private Long likeId;
        private Long postId;
        private String title;
        private Integer likeCount;
        private Integer commentCount;
        private LocalDateTime createdAt;
        private LocalDateTime likedAt;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class AccountInfo {
        private String email;
        private LocalDateTime passwordLastChanged;
        private Boolean newCommentNotification;
        private Boolean likeNoticeNotification;
    }
}
