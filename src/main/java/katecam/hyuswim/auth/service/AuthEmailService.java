package katecam.hyuswim.auth.service;

import katecam.hyuswim.auth.dto.EmailSendRequest;
import katecam.hyuswim.auth.dto.EmailSendResponse;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthEmailService {

    private final RedisTemplate<String, String> redisTemplate;
    private final EmailSenderService emailSenderService;

    private static final Duration CODE_TTL = Duration.ofMinutes(3);
    private static final Duration COOLDOWN_TTL = Duration.ofMinutes(1);
    private static final Duration LIMIT_DURATION = Duration.ofHours(1);
    private static final int MAX_REQUESTS = 5;

    public EmailSendResponse sendCode(EmailSendRequest request, String ip) {
        String email = request.getEmail();
        String cooldownKey = "auth:email:cooldown:" + email;
        String codeKey = "auth:email:" + email;

        // 요청 횟수 제한 (이메일 + IP)
        checkRequestLimit(email, ip);

        // 1. 기존 코드 살아있으면 재사용
        String existingCode = redisTemplate.opsForValue().get(codeKey);
        if (existingCode != null) {
            // 쿨다운 중인 경우에도 같은 코드 재발송
            if (Boolean.TRUE.equals(redisTemplate.hasKey(cooldownKey))) {
                resendEmail(email, existingCode);
                return new EmailSendResponse(false); // 기존 코드 재사용
            }
            // 쿨다운 없으면 새 쿨다운 부여
            redisTemplate.opsForValue().set(cooldownKey, "true", COOLDOWN_TTL);
            resendEmail(email, existingCode);
            return new EmailSendResponse(false); // 기존 코드 재사용
        }

        // 2. 새 코드 생성
        String newCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        redisTemplate.opsForValue().set(codeKey, newCode, CODE_TTL);
        redisTemplate.opsForValue().set(cooldownKey, "true", COOLDOWN_TTL);

        resendEmail(email, newCode);
        return new EmailSendResponse(true); // 새 코드 발급
    }

    private void resendEmail(String email, String code) {
        String mailText = """
            안녕하세요, 휴쉼입니다 🌱

            이메일 인증을 위해 아래 인증번호를 입력해 주세요.

            [ 인증번호 ]  %s

            ※ 본 인증번호는 발송 시점부터 3분간만 유효합니다.
            ※ 본 메일은 발신 전용입니다.
            """.formatted(code);

        emailSenderService.send(
                email,
                "[휴쉼] 이메일 인증 안내",
                mailText
        );
    }

    private void checkRequestLimit(String email, String ip) {
        // 이메일 제한
        String emailKey = "auth:email:limit:" + email;
        long emailCount = Objects.requireNonNull(redisTemplate.opsForValue().increment(emailKey));
        if (emailCount == 1L) {
            redisTemplate.expire(emailKey, LIMIT_DURATION);
        }
        if (emailCount > MAX_REQUESTS) {
            throw new CustomException(ErrorCode.EMAIL_REQUEST_LIMIT_EXCEEDED);
        }

        // IP 제한
        String ipKey = "auth:ip:limit:" + ip;
        long ipCount = Objects.requireNonNull(redisTemplate.opsForValue().increment(ipKey));
        if (ipCount == 1L) {
            redisTemplate.expire(ipKey, LIMIT_DURATION);
        }
        if (ipCount > MAX_REQUESTS) {
            throw new CustomException(ErrorCode.IP_REQUEST_LIMIT_EXCEEDED);
        }
    }

    public void verifyCode(String email, String code) {
        String key = "auth:email:" + email;
        String saved = redisTemplate.opsForValue().get(key);

        if (saved == null) {
            throw new CustomException(ErrorCode.EMAIL_CODE_EXPIRED);
        }

        if (!saved.equals(code)) {
            throw new CustomException(ErrorCode.EMAIL_CODE_MISMATCH);
        }

        // 인증 성공 → verified 플래그 저장 (5분)
        redisTemplate.opsForValue()
                .set("auth:email:verified:" + email, "true", Duration.ofMinutes(5));

        redisTemplate.delete(key);
    }
}


