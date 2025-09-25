package katecam.hyuswim.counseling.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.hyuswim.ai.dto.Message;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.counseling.repository.CounselingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CounselingSessionService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CounselingSessionRepository counselingSessionRepository;

    private static final String PREFIX = "session:";
    private static final String MESSAGES_SUFFIX = ":messages";
    private static final String STEP_SUFFIX = ":step";
    private static final Duration TTL = Duration.ofMinutes(20);

    public void saveMessage(String sessionId, Message message) {
        String key = buildKey(sessionId, MESSAGES_SUFFIX);
        String json = toJson(message);
        try {
            redisTemplate.opsForList().rightPush(key, json);
            redisTemplate.expire(key, TTL);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    public List<Message> getMessages(String sessionId) {
        String key = buildKey(sessionId, MESSAGES_SUFFIX);
        List<String> jsonList;
        try {
            jsonList = redisTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }

        if (jsonList == null || jsonList.isEmpty()) return List.of();

        return jsonList.stream()
                .map(this::fromJson)
                .collect(Collectors.toList());
    }

    public void saveStep(String sessionId, String step) {
        String key = buildKey(sessionId, STEP_SUFFIX);
        try {
            redisTemplate.opsForValue().set(key, step, TTL);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    public String getStep(String sessionId) {
        String key = buildKey(sessionId, STEP_SUFFIX);
        try {
            String step = redisTemplate.opsForValue().get(key);
            return step != null ? step : "START";
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    public void endSession(String sessionId) {
        try {
            counselingSessionRepository.findById(sessionId)
                    .ifPresent(session -> {
                        session.end(); // endedAt = now
                        counselingSessionRepository.save(session);
                    });

            redisTemplate.delete(buildKey(sessionId, MESSAGES_SUFFIX));
            redisTemplate.delete(buildKey(sessionId, STEP_SUFFIX));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    private String buildKey(String sessionId, String suffix) {
        return PREFIX + sessionId + suffix;
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_SERIALIZATION_FAILED);
        }
    }

    private Message fromJson(String json) {
        try {
            return objectMapper.readValue(json, Message.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_DESERIALIZATION_FAILED);
        }
    }
}


