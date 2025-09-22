package katecam.hyuswim.admin.controller;

import katecam.hyuswim.admin.service.AdminReportService;
import katecam.hyuswim.report.domain.ReportStatus;
import katecam.hyuswim.report.dto.ReportDetailResponse;
import katecam.hyuswim.report.dto.ReportListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class AdminReportController {

    private final AdminReportService adminReportService;

    @GetMapping
    public String reports(Model model) {
        List<ReportListResponse> reports = adminReportService.getReports();
        model.addAttribute("reports", reports);
        return "admin/reports/list";
    }

    @GetMapping("/{reportId}")
    public String report(@PathVariable Long reportId, Model model) {
        ReportDetailResponse report = adminReportService.getReport(reportId);
        model.addAttribute("report", report);
        return "admin/reports/detail";
    }

    @PostMapping("/{reportId}/status")
    public String updateStatus(@PathVariable Long reportId,
                               @RequestParam ReportStatus status,
                               RedirectAttributes ra) {
        adminReportService.updateStatus(reportId, status);
        ra.addFlashAttribute("msg", "신고 상태가 업데이트되었습니다.");
        return "redirect:/admin/reports/" + reportId;
    }
}
