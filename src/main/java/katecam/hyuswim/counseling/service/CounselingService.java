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
    private final CounselingSessionService sessionService;
    private final ObjectMapper objectMapper;

    private static final int MAX_TURNS = 10;
    private static final String END_TOKEN = "[END_SESSION]";

    public CounselingResponse startSession() {
        CounselingSession session = new CounselingSession(CounselingStep.ACTIVE);
        sessionRepository.save(session);

        String greeting = openAiClient.generateCounselingReply(List.of(), CounselingStep.ACTIVE);
        String cleanGreeting = sanitizeReply(greeting);

        sessionService.saveMessage(session.getId(), new Message("assistant", cleanGreeting));
        sessionService.saveStep(session.getId(), CounselingStep.ACTIVE.name());

        return new CounselingResponse(session.getId(), cleanGreeting, CounselingStep.ACTIVE.name());
    }

    public CounselingResponse processMessage(String sessionId, String userMessage) {
        CounselingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        List<Message> history = new ArrayList<>(sessionService.getMessages(sessionId));

        history.add(new Message("user", userMessage));
        sessionService.saveMessage(sessionId, new Message("user", userMessage));

        long userTurns = history.stream().filter(m -> "user".equals(m.role())).count();
        if (userTurns >= MAX_TURNS) {
            return closeSession(session, "오늘은 충분히 얘기 나눈 것 같아, 여기서 마무리하자" + END_TOKEN);
        }

        String reply = openAiClient.generateCounselingReply(history, session.getStep());
        String cleanReply = sanitizeReply(reply);

        sessionService.saveMessage(sessionId, new Message("assistant", cleanReply));

        if (isClosingReply(reply)) {
            return closeSession(session, cleanReply);
        } else {
            session.updateStep(CounselingStep.ACTIVE);
            sessionRepository.save(session);
            sessionService.saveStep(sessionId, CounselingStep.ACTIVE.name());
            return new CounselingResponse(sessionId, cleanReply, CounselingStep.ACTIVE.name());
        }
    }

    public void endSession(String sessionId) {
        CounselingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        session.end();
        sessionRepository.save(session);
        sessionService.endSession(sessionId);
    }

    private CounselingResponse closeSession(CounselingSession session, String closingReply) {
        String clean = sanitizeReply(closingReply);
        session.end();
        sessionRepository.save(session);
        sessionService.saveMessage(session.getId(), new Message("assistant", clean));
        sessionService.endSession(session.getId());
        return new CounselingResponse(session.getId(), clean, CounselingStep.CLOSED.name());
    }

    private boolean isClosingReply(String reply) {
        return reply != null && reply.contains(END_TOKEN);
    }

    private String sanitizeReply(String reply) {
        return reply == null ? "" : reply.replace(END_TOKEN, "").trim();
    }
}

