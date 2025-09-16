package katecam.hyuswim.admin.controller;

import katecam.hyuswim.admin.service.AdminReportService;
import katecam.hyuswim.report.domain.ReportStatus;
import katecam.hyuswim.report.dto.ReportDetailResponse;
import katecam.hyuswim.report.dto.ReportListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class AdminReportController {

    private final AdminReportService adminReportService;


    @GetMapping
    public ResponseEntity<List<ReportListResponse>> getReports() {
        return ResponseEntity.ok(adminReportService.getReports());
    }


    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDetailResponse> getReport(@PathVariable Long reportId) {
        return ResponseEntity.ok(adminReportService.getReport(reportId));
    }


    @PatchMapping("/{reportId}/status")
    public ResponseEntity<ReportDetailResponse> updateStatus(
            @PathVariable Long reportId,
            @RequestParam ReportStatus status
    ) {
        return ResponseEntity.ok(adminReportService.updateStatus(reportId, status));
    }
}
