package katecam.hyuswim.report.dto;

import java.time.LocalDateTime;

import katecam.hyuswim.report.domain.Report;
import katecam.hyuswim.report.domain.ReportReasonType;
import katecam.hyuswim.report.domain.ReportStatus;
import katecam.hyuswim.report.domain.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDetailResponse {
    private Long id;
    private ReportType reportType;
    private Long targetId;
    private String reasonCode;
    private String reasonDisplayName;
    private String content;
    private String reporter;
    private String reportedUser;
    private ReportReasonType reportReasonType;
    private ReportStatus status;
    private LocalDateTime reportedAt;

    public static ReportDetailResponse from(Report report) {
        ReportReasonType reasonType = report.getReportReasonType();

        return ReportDetailResponse.builder()
                .id(report.getId())
                .reportType(report.getReportType())
                .targetId(report.getTargetId())
                .reasonCode(reasonType != null ? reasonType.name() : "UNKNOWN")
                .reasonDisplayName(reasonType != null ? reasonType.getDisplayName() : "미지정")
                .content(report.getContent())
                .reporter(report.getReporter() != null ? report.getReporter().getNickname() : "알 수 없음")
                .reportedUser(report.getReportedUser() != null ? report.getReportedUser().getNickname() : "알 수 없음")
                .reportReasonType(reasonType)
                .status(report.getStatus())
                .reportedAt(report.getReportedAt())
                .build();
    }
}
