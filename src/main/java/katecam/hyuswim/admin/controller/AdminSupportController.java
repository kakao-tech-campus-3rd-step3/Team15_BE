package katecam.hyuswim.admin.controller;

import katecam.hyuswim.admin.controller.dto.CreateSupportRequest;
import katecam.hyuswim.admin.service.AdminSupportService;
import katecam.hyuswim.support.domain.Support;
import katecam.hyuswim.support.domain.SupportType;
import katecam.hyuswim.support.dto.SupportDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/support-links")
public class AdminSupportController {

    private final AdminSupportService adminSupportService;

    @PostMapping
    public ResponseEntity<SupportDetailResponse> createSupport(@Validated @RequestBody CreateSupportRequest req) {

        // Support 엔티티 빌더에 맞게 필드만 매핑
        Support support = Support.builder()
                .name(req.name())
                .company(req.company())
                .content(req.content())
                .place(req.place())
                .endDate(req.endDate())
                .supportType(parseSupportType(req.supportType()))
                .url(req.applicationUrl()) // ✅ applicationUrl → url 필드로 매핑
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminSupportService.createSupport(support));
    }

    // 문자열로 넘어온 지원사업 유형을 Enum으로 안전하게 변환
    private SupportType parseSupportType(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }
        try {
            return SupportType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SupportDetailResponse> updateSupport(@PathVariable Long id,
                                                               @RequestBody Support updateRequest) {
        return ResponseEntity.ok(adminSupportService.updateSupport(id, updateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupport(@PathVariable Long id) {
        adminSupportService.deleteSupport(id);
        return ResponseEntity.noContent().build();
    }
}
