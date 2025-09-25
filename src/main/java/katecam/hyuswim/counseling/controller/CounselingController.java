package katecam.hyuswim.counseling.controller;

import katecam.hyuswim.counseling.dto.CounselingRequest;
import katecam.hyuswim.counseling.dto.CounselingResponse;
import katecam.hyuswim.counseling.service.CounselingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/counsel")
@RequiredArgsConstructor
public class CounselingController {

    private final CounselingService counselingService;

    @PostMapping("/start")
    public ResponseEntity<CounselingResponse> start() {
        CounselingResponse response = counselingService.startSession();
        return ResponseEntity.ok()
                .header("X-Session-Id", response.getSessionId())
                .body(response);
    }

    @PostMapping("/{sessionId}/message")
    public ResponseEntity<CounselingResponse> chat(
            @PathVariable String sessionId,
            @RequestBody CounselingRequest request
    ) {
        CounselingResponse response = counselingService.processMessage(sessionId, request.getMessage());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> end(@PathVariable String sessionId) {
        counselingService.endSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}


