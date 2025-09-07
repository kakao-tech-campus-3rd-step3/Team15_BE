package katecam.hyuswim.user.dto;

import katecam.hyuswim.user.User;
import lombok.*;

@Getter
@AllArgsConstructor
public class SignupRequest {

    private String username;
    private String password;
    private String nickname;

}
