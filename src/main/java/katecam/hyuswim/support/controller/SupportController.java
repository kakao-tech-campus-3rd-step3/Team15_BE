package katecam.hyuswim.support.controller;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
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
    @GetMapping("/type/{type}")
    public List<SupportResponse> getSupportsByType(@PathVariable String type) {
        try {
            SupportType supportType = SupportType.valueOf(type.toUpperCase());
            return supportService.getSupportsByType(supportType);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_SUPPORT_TYPE);
        }
    }

    // 지원 사업 상세 조회
    @GetMapping("/{programId}")
    public SupportDetailResponse getSupportDetail(@PathVariable Long programId) {
        return supportService.getSupportDetail(programId);
    }
}
