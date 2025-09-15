package katecam.hyuswim.admin.dto;

import java.time.LocalDateTime;

import katecam.hyuswim.user.UserStatus;

public class BlockResponse {

  private final Long userId;
  private final UserStatus status;
  private final String type;
  private final LocalDateTime until;
  private final String reason;
  private final String message;

  public BlockResponse(
      Long userId,
      UserStatus status,
      String type,
      LocalDateTime until,
      String reason,
      String message) {
    this.userId = userId;
    this.status = status;
    this.type = type;
    this.until = until;
    this.reason = reason;
    this.message = message;
  }

  public Long getUserId() {
    return userId;
  }

  public UserStatus getStatus() {
    return status;
  }

  public String getType() {
    return type;
  }

  public LocalDateTime getUntil() {
    return until;
  }

  public String getReason() {
    return reason;
  }

  public String getMessage() {
    return message;
  }
}
