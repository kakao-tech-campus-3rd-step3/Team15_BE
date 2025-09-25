package katecam.hyuswim.support.controller;

import katecam.hyuswim.support.domain.SupportType;
import katecam.hyuswim.support.dto.SupportResponse;
import katecam.hyuswim.support.dto.SupportDetailResponse;
import katecam.hyuswim.support.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/support-programs")
public class SupportController {

    private final SupportService supportService;

    @GetMapping
    public ResponseEntity<List<SupportResponse>> getAllSupports() {
        return ResponseEntity.ok(supportService.getAllSupports());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<SupportResponse>> getSupportsByType(@PathVariable SupportType type) {
        return ResponseEntity.ok(supportService.getSupportsByType(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportDetailResponse> getSupportDetail(@PathVariable Long id) {
        return ResponseEntity.ok(supportService.getSupportDetail(id));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getActiveSupportCount() {
        return ResponseEntity.ok(Map.of("count", supportService.getActiveSupportCount()));
    }
}

