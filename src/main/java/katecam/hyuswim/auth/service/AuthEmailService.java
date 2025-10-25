package katecam.hyuswim.auth.service;

import katecam.hyuswim.auth.dto.EmailSendRequest;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthEmailService {

    private final RedisTemplate<String, String> redisTemplate;
    private final EmailSenderService emailSenderService;
    private static final SecureRandom secureRandom = new SecureRandom();

    private static final Duration CODE_TTL = Duration.ofMinutes(3);

    public void sendCode(EmailSendRequest request) {
        String email = request.getEmail();
        String codeKey = "auth:email:" + email;

        String newCode = generate6DigitCode();

        redisTemplate.opsForValue().set(codeKey, newCode, CODE_TTL);

        sendEmail(email, newCode);
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

        redisTemplate.opsForValue()
                .set("auth:email:verified:" + email, "true", Duration.ofMinutes(5));

        redisTemplate.delete(key);
    }

    private void sendEmail(String email, String code) {
        String mailText = """
            안녕하세요, 휴쉼입니다.

            이메일 인증을 위해 아래 인증번호를 입력해 주세요.

            [ 인증번호 ]  %s

            ※ 본 인증번호는 발송 시점부터 3분간만 유효합니다.
            ※ 본 메일은 발신 전용입니다.
            """.formatted(code);

        emailSenderService.send(email, "[휴쉼] 이메일 인증 안내", mailText);
    }

    private String generate6DigitCode() {
        return String.valueOf(secureRandom.nextInt(900000) + 100000);
    }
}
