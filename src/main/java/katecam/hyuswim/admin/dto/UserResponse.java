package katecam.hyuswim.admin.dto;

import java.util.List;

import katecam.hyuswim.user.UserStatus;

public class UserResponse {

  private final Long userId;
  private final String email;
  private final UserStatus status;

  private final List<PostSummary> posts;
  private final List<CommentSummary> comments;

  public UserResponse(
      Long userId,
      String email,
      UserStatus status,
      List<PostSummary> posts,
      List<CommentSummary> comments) {
    this.userId = userId;
    this.email = email;
    this.status = status;
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

  public List<PostSummary> getPosts() {
    return posts;
  }

  public List<CommentSummary> getComments() {
    return comments;
  }

  public static class PostSummary {
    private final Long postId;
    private final String title;
    private final java.time.LocalDateTime createdAt;

    public PostSummary(Long postId, String title, java.time.LocalDateTime createdAt) {
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

    public java.time.LocalDateTime getCreatedAt() {
      return createdAt;
    }
  }

  public static class CommentSummary {
    private final Long commentId;
    private final String content;
    private final java.time.LocalDateTime createdAt;

    public CommentSummary(Long commentId, String content, java.time.LocalDateTime createdAt) {
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

    public java.time.LocalDateTime getCreatedAt() {
      return createdAt;
    }
  }
}
