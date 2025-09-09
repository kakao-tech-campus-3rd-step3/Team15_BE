package katecam.hyuswim.auth.jwt;

import katecam.hyuswim.user.UserRole;
import lombok.*;

@Getter
@AllArgsConstructor
public class JwtTokenRequest {

  private String email;
  private UserRole role;
}
