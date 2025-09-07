package katecam.hyuswim.user.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class LoginRequest {

    private String username;
    private String password;

}
