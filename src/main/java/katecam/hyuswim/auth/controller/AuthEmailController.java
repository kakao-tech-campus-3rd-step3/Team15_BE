package katecam.hyuswim.auth.controller;

import jakarta.validation.Valid;
import katecam.hyuswim.auth.dto.EmailSendRequest;
import katecam.hyuswim.auth.dto.EmailVerifyRequest;
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
    public ResponseEntity<Void> sendCode(
            @Valid @RequestBody EmailSendRequest request
    ) {
        authEmailService.sendCode(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody EmailVerifyRequest request) {
        authEmailService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok().build();
    }
}

