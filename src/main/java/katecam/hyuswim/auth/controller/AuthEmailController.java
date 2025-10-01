package katecam.hyuswim.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import katecam.hyuswim.auth.dto.EmailSendRequest;
import katecam.hyuswim.auth.dto.EmailSendResponse;
import katecam.hyuswim.auth.dto.EmailVerifyRequest;
import katecam.hyuswim.auth.service.AuthEmailService;
import katecam.hyuswim.common.util.IpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/email")
@RequiredArgsConstructor
public class AuthEmailController {

    private final AuthEmailService authEmailService;

    @PostMapping("/send")
    public ResponseEntity<EmailSendResponse> sendCode(
            @Valid @RequestBody EmailSendRequest request,
            HttpServletRequest httpRequest
    ) {
        String clientIp = IpUtils.getClientIp(httpRequest);

        EmailSendResponse response = authEmailService.sendCode(request, clientIp);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody EmailVerifyRequest request) {
        authEmailService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok().build();
    }
}

