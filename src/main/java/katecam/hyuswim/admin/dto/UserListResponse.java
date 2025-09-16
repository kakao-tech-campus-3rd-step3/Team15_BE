package katecam.hyuswim.admin.dto;

import katecam.hyuswim.user.domain.UserStatus;

public class UserListResponse {
    private final Long userId;
    private final String email;
    private final UserStatus status;

    public UserListResponse(Long userId, String email, UserStatus status) {
        this.userId = userId;
        this.email = email;
        this.status = status;
    }

    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public UserStatus getStatus() { return status; }
}
