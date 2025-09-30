package katecam.hyuswim.user.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordUpdateRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
