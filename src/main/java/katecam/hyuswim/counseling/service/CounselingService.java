package katecam.hyuswim.counseling.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.hyuswim.ai.client.OpenAiClient;
import katecam.hyuswim.ai.dto.Message;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.counseling.domain.CounselingSession;
import katecam.hyuswim.counseling.domain.CounselingStep;
import katecam.hyuswim.counseling.dto.CounselingResponse;
import katecam.hyuswim.counseling.repository.CounselingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CounselingService {

    private final CounselingSessionRepository sessionRepository;
    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    private static final int MAX_TURNS = 10;
    private static final String END_TOKEN = "[END_SESSION]";

    public CounselingResponse startSession() {
        CounselingSession session = new CounselingSession(CounselingStep.ACTIVE);
        sessionRepository.save(session);

        String greeting = openAiClient.generateCounselingReply(List.of(), CounselingStep.ACTIVE);
        String cleanGreeting = sanitizeReply(greeting);

        return new CounselingResponse(session.getId(), cleanGreeting, session.getStep().name());
    }

    public CounselingResponse processMessage(String sessionId, String userMessage) {
        CounselingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        List<Message> history = new ArrayList<>();
        history.add(new Message("user", userMessage));

        String reply = openAiClient.generateCounselingReply(history, session.getStep());
        String cleanReply = sanitizeReply(reply);

        if (isClosingReply(reply)) {
            session.end();
        } else {
            session.updateStep(CounselingStep.ACTIVE);
        }

        sessionRepository.save(session);

        return new CounselingResponse(session.getId(), cleanReply, session.getStep().name());
    }

    public void endSession(String sessionId) {
        CounselingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        session.end();
        sessionRepository.save(session);
    }

    private boolean isClosingReply(String reply) {
        return reply != null && reply.contains(END_TOKEN);
    }

    private String sanitizeReply(String reply) {
        return reply == null ? "" : reply.replace(END_TOKEN, "").trim();
    }
}
