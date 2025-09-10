package katecam.hyuswim.auth.jwt;

import katecam.hyuswim.user.UserRole;
import lombok.*;

@Getter
@AllArgsConstructor
public class JwtTokenRequest {

    private Long userId;
    private UserRole role;
}
