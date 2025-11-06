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
    private static final Duration TTL = Duration.ofMinutes(20);

    public void saveMessages(String sessionId, List<Message> messages) {
        String key = buildKey(sessionId);
        try {
            List<String> jsonList = messages.stream()
                    .map(m -> {
                        try {
                            return objectMapper.writeValueAsString(m);
                        } catch (Exception e) {
                            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
                        }
                    })
                    .toList();

            redisTemplate.opsForList().rightPushAll(key, jsonList);
            redisTemplate.expire(key, TTL);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    public List<Message> getMessages(String sessionId) {
        String key = buildKey(sessionId);
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

    public void endSession(String sessionId) {
        try {
            redisTemplate.delete(buildKey(sessionId));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    private String buildKey(String sessionId) {
        return PREFIX + sessionId + MESSAGES_SUFFIX;
    }

    private Message fromJson(String json) {
        try {
            return objectMapper.readValue(json, Message.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_DESERIALIZATION_FAILED);
        }
    }
}
