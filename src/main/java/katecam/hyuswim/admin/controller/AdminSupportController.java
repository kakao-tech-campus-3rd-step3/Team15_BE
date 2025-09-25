package katecam.hyuswim.admin.controller;

import katecam.hyuswim.support.domain.Support;
import katecam.hyuswim.support.dto.SupportDetailResponse;
import katecam.hyuswim.admin.service.AdminSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/support-links")
public class AdminSupportController {

    private final AdminSupportService adminSupportService;

    // 신규 지원 사업 등록
    @PostMapping
    public ResponseEntity<SupportDetailResponse> createSupport(@RequestBody Support support) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminSupportService.createSupport(support));
    }

    // 지원 사업 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<SupportDetailResponse> updateSupport(@PathVariable Long id,
                                                               @RequestBody Support updateRequest) {
        return ResponseEntity.ok(adminSupportService.updateSupport(id, updateRequest));
    }

    // 지원 사업 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupport(@PathVariable Long id) {
        adminSupportService.deleteSupport(id);
        return ResponseEntity.noContent().build();
    }
}
