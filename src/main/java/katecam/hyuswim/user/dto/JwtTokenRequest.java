package katecam.hyuswim.user.dto;

import katecam.hyuswim.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtTokenRequest {

    private String username;
    private UserRole role;


}
