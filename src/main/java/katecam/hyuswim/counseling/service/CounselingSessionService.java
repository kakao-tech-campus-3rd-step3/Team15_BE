package katecam.hyuswim.counseling.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.hyuswim.ai.dto.Message;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
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

    private static final String PREFIX = "session:";
    private static final String MESSAGES_SUFFIX = ":messages";
    private static final String STEP_SUFFIX = ":step";
    private static final Duration TTL = Duration.ofMinutes(20);

    public void saveMessage(String sessionId, Message message) {
        String key = buildKey(sessionId, MESSAGES_SUFFIX);
        try {
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(key, json);
            redisTemplate.expire(key, TTL);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    public List<Message> getMessages(String sessionId) {
        String key = buildKey(sessionId, MESSAGES_SUFFIX);
        try {
            List<String> jsonList = redisTemplate.opsForList().range(key, 0, -1);
            if (jsonList == null || jsonList.isEmpty()) return List.of();
            return jsonList.stream()
                    .map(this::fromJson)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
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
            return step != null ? step : "ACTIVE";
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    public void endSession(String sessionId) {
        try {
            redisTemplate.delete(buildKey(sessionId, MESSAGES_SUFFIX));
            redisTemplate.delete(buildKey(sessionId, STEP_SUFFIX));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    private String buildKey(String sessionId, String suffix) {
        return PREFIX + sessionId + suffix;
    }

    private Message fromJson(String json) {
        try {
            return objectMapper.readValue(json, Message.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_DESERIALIZATION_FAILED);
        }
    }
}
