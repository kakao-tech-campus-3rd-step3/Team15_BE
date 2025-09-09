package katecam.hyuswim.admin.dto;

import katecam.hyuswim.user.UserStatus;
import java.time.LocalDateTime;
import java.util.List;

public class UserResponse {

    private final Long userId;
    private final String email;
    private final UserStatus status;
    private final LocalDateTime lastLoginAt;

    private final List<PostSummary> posts;
    private final List<CommentSummary> comments;

    public UserResponse(Long userId,
                        String email,
                        UserStatus status,
                        LocalDateTime lastLoginAt,
                        List<PostSummary> posts,
                        List<CommentSummary> comments) {
        this.userId = userId;
        this.email = email;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
        this.posts = posts;
        this.comments = comments;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public List<PostSummary> getPosts() {
        return posts;
    }

    public List<CommentSummary> getComments() {
        return comments;
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

        public Long getPostId() {
            return postId;
        }

        public String getTitle() {
            return title;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
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

        public Long getCommentId() {
            return commentId;
        }

        public String getContent() {
            return content;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }
}
