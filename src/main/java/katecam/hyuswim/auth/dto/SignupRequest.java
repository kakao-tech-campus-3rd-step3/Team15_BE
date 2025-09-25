package katecam.hyuswim.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
}
