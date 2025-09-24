package katecam.hyuswim.auth.dto;

import lombok.Getter;

@Getter
public class EmailResponse {
    private final String message;

    public EmailResponse(String message) {
        this.message = message;
    }
}
