package katecam.hyuswim.auth.controller;

import katecam.hyuswim.auth.dto.EmailSendRequest;
import katecam.hyuswim.auth.dto.EmailVerifyRequest;
import katecam.hyuswim.auth.dto.EmailResponse;
import katecam.hyuswim.auth.service.AuthEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/email")
@RequiredArgsConstructor
public class AuthEmailController {

    private final AuthEmailService authEmailService;

    @PostMapping("/send")
    public ResponseEntity<EmailResponse> sendCode(@RequestBody EmailSendRequest request) {
        authEmailService.sendCode(request);
        return ResponseEntity.ok(new EmailResponse("인증번호 발송 완료"));
    }

    @PostMapping("/verify")
    public ResponseEntity<EmailResponse> verifyCode(@RequestBody EmailVerifyRequest request) {
        authEmailService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(new EmailResponse("인증 성공"));
    }
}

