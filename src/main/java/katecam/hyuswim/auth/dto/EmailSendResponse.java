package katecam.hyuswim.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailSendResponse {
    private final boolean isNewCode;
}
