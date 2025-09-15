package katecam.hyuswim.user.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyProfileEditResponse {

    private String profileImage;
    private String nickname;
    private String introduction;
}
