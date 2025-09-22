package katecam.hyuswim.admin.dto;

import java.time.LocalDateTime;
import java.util.List;
import katecam.hyuswim.user.domain.UserStatus;
import lombok.Getter;

@Getter
public class UserDetailResponse {
    private final Long userId;
    private final UserStatus status;
    private final List<PostSummary> posts;
    private final List<CommentSummary> comments;

    public UserDetailResponse(Long userId, UserStatus status,
                              List<PostSummary> posts, List<CommentSummary> comments) {
        this.userId = userId;
        this.status = status;
        this.posts = posts;
        this.comments = comments;
    }

    public static class PostSummary {
        private final Long postId;
        private final String title;
        private final LocalDateTime createdAt;

        public PostSummary(Long postId, String title, LocalDateTime createdAt) {
            this.postId = postId;
            this.title = title;
            this.createdAt = createdAt;
        }
        public Long getPostId() { return postId; }
        public String getTitle() { return title; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }

    public static class CommentSummary {
        private final Long commentId;
        private final String content;
        private final LocalDateTime createdAt;

        public CommentSummary(Long commentId, String content, LocalDateTime createdAt) {
            this.commentId = commentId;
            this.content = content;
            this.createdAt = createdAt;
        }
        public Long getCommentId() { return commentId; }
        public String getContent() { return content; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }
}
