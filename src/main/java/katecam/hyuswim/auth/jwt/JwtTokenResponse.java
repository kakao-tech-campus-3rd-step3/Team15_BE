package katecam.hyuswim.auth.jwt;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class JwtTokenResponse {
  private final String token;
}
