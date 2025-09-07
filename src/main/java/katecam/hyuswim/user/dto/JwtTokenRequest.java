package katecam.hyuswim.user.dto;

import katecam.hyuswim.user.UserRole;
import lombok.*;

@Getter
@AllArgsConstructor
public class JwtTokenRequest {

    private String username;
    private UserRole role;


}
