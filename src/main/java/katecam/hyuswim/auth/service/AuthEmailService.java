package katecam.hyuswim.auth.service;

import katecam.hyuswim.auth.dto.EmailSendRequest;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthEmailService {

    private final RedisTemplate<String, String> redisTemplate;
    private final EmailSenderService emailSenderService;

    public void sendCode(EmailSendRequest request) {
        String email = request.getEmail();
        String code = String.valueOf((int)(Math.random() * 900000) + 100000);

        try {
            // Redis에 인증번호 저장 (5분)
            redisTemplate.opsForValue()
                    .set("auth:email:" + email, code, Duration.ofMinutes(5));

            String mailText = """
                    안녕하세요, 휴쉼입니다 🌱

                    이메일 인증을 위해 아래 인증번호를 입력해 주세요.

                    [ 인증번호 ]  %s

                    ※ 본 인증번호는 발송 시점부터 5분간만 유효합니다.
                    ※ 본 메일은 발신 전용입니다.
                    """.formatted(code);

            emailSenderService.send(
                    email,
                    "[휴쉼] 이메일 인증 안내",
                    mailText
            );

        } catch (Exception e) {
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
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

        redisTemplate.delete(key);
    }
}


