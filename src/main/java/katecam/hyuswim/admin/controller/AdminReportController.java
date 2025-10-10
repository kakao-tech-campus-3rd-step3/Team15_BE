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
    private static final String ACTIVE_MENU = "reports";

    @GetMapping
    public String reports(Model model) {
        List<ReportListResponse> reports = adminReportService.getReports();
        model.addAttribute("reports", reports);
        model.addAttribute("pageTitle", "신고 관리");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        // [수정] 레이아웃이 아닌 콘텐츠 페이지의 경로를 반환합니다.
        return "admin/reports/list";
    }

    @GetMapping("/{reportId}")
    public String report(@PathVariable Long reportId, Model model) {
        ReportDetailResponse report = adminReportService.getReport(reportId);
        model.addAttribute("report", report);
        model.addAttribute("pageTitle", "신고 상세");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        // [수정] 레이아웃이 아닌 콘텐츠 페이지의 경로를 반환합니다.
        return "admin/reports/detail";
    }

    @PostMapping("/{reportId}/status")
    public String updateStatus(@PathVariable Long reportId,
                               @RequestParam ReportStatus status,
                               RedirectAttributes ra) {
        adminReportService.updateStatus(reportId, status);
        ra.addFlashAttribute("msg", "신고 상태가 업데이트되었습니다.");
        // redirect는 그대로 유지합니다.
        return "redirect:/admin/reports/" + reportId;
    }
}
