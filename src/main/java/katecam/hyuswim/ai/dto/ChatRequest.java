package katecam.hyuswim.ai.dto;

import java.util.List;

public record ChatRequest(
        String model,
        List<Message> messages,
        int max_tokens
) {}
