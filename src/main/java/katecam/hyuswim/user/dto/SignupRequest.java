package katecam.hyuswim.user.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class SignupRequest {

  private String email;
  private String password;
}
