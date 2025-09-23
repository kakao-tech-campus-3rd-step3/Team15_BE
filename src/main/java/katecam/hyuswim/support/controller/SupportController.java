package katecam.hyuswim.support.controller;

import katecam.hyuswim.support.domain.SupportType;
import katecam.hyuswim.support.dto.SupportResponse;
import katecam.hyuswim.support.dto.SupportDetailResponse;
import katecam.hyuswim.support.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/support-programs")
public class SupportController {

    private final SupportService supportService;

    // 지원 사업 목록 조회
    @GetMapping
    public List<SupportResponse> getAllSupports() {
        return supportService.getAllSupports();
    }

    // 지원 사업 카테고리 조회
    @GetMapping("/{type}")
    public List<SupportResponse> getSupportsByType(@PathVariable SupportType type) {
        return supportService.getSupportsByType(type);
    }

    // 지원 사업 상세 조회
    @GetMapping("/{programId}/detail")
    public SupportDetailResponse getSupportDetail(@PathVariable Long programId) {
        return supportService.getSupportDetail(programId);
    }
}
