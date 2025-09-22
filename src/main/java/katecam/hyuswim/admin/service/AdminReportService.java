package katecam.hyuswim.admin.service;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.report.domain.Report;
import katecam.hyuswim.report.domain.ReportStatus;
import katecam.hyuswim.report.dto.ReportDetailResponse;
import katecam.hyuswim.report.dto.ReportListResponse;
import katecam.hyuswim.report.repository.ReportRepository;
import katecam.hyuswim.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final ReportRepository reportRepository;

    public List<ReportListResponse> getReports() {
        return reportRepository.findAll().stream()
                .map(ReportListResponse::from)
                .toList();
    }

    public ReportDetailResponse getReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
        return ReportDetailResponse.from(report);
    }


    public ReportDetailResponse updateStatus(Long reportId, ReportStatus status) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
        report.updateStatus(status);
        return ReportDetailResponse.from(reportRepository.save(report));
    }
}
