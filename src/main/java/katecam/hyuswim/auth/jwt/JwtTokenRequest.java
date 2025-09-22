package katecam.hyuswim.auth.jwt;

import katecam.hyuswim.user.domain.UserRole;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class JwtTokenRequest {
  private final Long userId;
  private final UserRole role;
}
