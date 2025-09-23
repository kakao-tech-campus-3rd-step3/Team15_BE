package katecam.hyuswim.counseling.service;

import com.fasterxml.jackson.core.type.TypeReference;
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

        String greeting = openAiClient.generateCounselingReply(List.of(), CounselingStep.ACTIVE);
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("assistant", greeting));

        session.updateMessages(toJson(messages));
        sessionRepository.save(session);

        return new CounselingResponse(session.getId(), greeting, session.getStep().name());
    }

    public CounselingResponse processMessage(String sessionId, String userMessage) {
        CounselingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        List<Message> history = fromJson(session.getMessages());
        history.add(new Message("user", userMessage));

        int turnCount = history.size() / 2;

        if (turnCount >= MAX_TURNS) {
            return closeSession(session, history,
                    "오늘은 충분히 얘기 나눈 것 같아, 여기서 마무리하자" + END_TOKEN);
        }

        String reply = openAiClient.generateCounselingReply(history, session.getStep());
        history.add(new Message("assistant", reply));
        session.updateMessages(toJson(history));

        if (isClosingReply(reply)) {
            session.end();
        } else {
            session.updateStep(CounselingStep.ACTIVE);
        }

        sessionRepository.save(session);

        return new CounselingResponse(session.getId(), reply, session.getStep().name());
    }

    public void endSession(String sessionId) {
        CounselingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        session.end();
        sessionRepository.save(session);
    }

    private CounselingResponse closeSession(CounselingSession session, List<Message> history, String closingReply) {
        history.add(new Message("assistant", closingReply));
        session.updateMessages(toJson(history));
        session.end();
        sessionRepository.save(session);

        return new CounselingResponse(session.getId(), closingReply, CounselingStep.CLOSED.name());
    }

    private boolean isClosingReply(String reply) {
        return reply.contains(END_TOKEN);
    }

    private String toJson(List<Message> messages) {
        try {
            return objectMapper.writeValueAsString(messages);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_SERIALIZATION_FAILED);
        }
    }

    private List<Message> fromJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Message>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
