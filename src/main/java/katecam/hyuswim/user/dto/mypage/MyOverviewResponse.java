package katecam.hyuswim.user.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyOverviewResponse {

    private int postCount;
    private int commentCount;
    private int likesCount;
    private int MissionsCount;
    private String email;
}
