package katecam.hyuswim.admin.dto;

import java.time.LocalDateTime;

public class BlockRequest {

    private final String type;          
    private final LocalDateTime until;
    private final String reason;

    public BlockRequest(String type, LocalDateTime until, String reason) {
        this.type = type;
        this.until = until;
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getUntil() {
        return until;
    }

    public String getReason() {
        return reason;
    }
}
