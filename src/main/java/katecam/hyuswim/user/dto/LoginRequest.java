package katecam.hyuswim.user.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class LoginRequest {

  private String email;
  private String password;
}
