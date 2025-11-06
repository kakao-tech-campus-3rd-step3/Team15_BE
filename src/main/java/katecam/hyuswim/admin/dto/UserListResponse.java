package katecam.hyuswim.admin.dto;

import katecam.hyuswim.user.domain.UserStatus;
import lombok.Getter;

@Getter
public class UserListResponse {
    private final Long userId;
    private final String userName;
    private final UserStatus status;

    public UserListResponse(Long userId, String userName,UserStatus status) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
    }

    public Long getUserId() { return userId; }
    public UserStatus getStatus() { return status; }
    public String getUserName() { return userName; }
}
