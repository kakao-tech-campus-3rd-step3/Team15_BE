package katecam.hyuswim.auth.dto;

import katecam.hyuswim.auth.domain.UserAuth;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private Long id;

    private String handle;
    private String nickname;
    private UserRole role;
    private LocalDateTime createdAt;
    public static SignupResponse from(User user) {
        return new SignupResponse(
                user.getId(),
                user.getHandle(),
                user.getNickname(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
