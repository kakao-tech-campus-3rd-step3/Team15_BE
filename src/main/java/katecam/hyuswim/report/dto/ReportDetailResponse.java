package katecam.hyuswim.report.dto;

import java.time.LocalDateTime;

import katecam.hyuswim.report.domain.ReportReasonType;
import katecam.hyuswim.report.domain.Report;
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
    return ReportDetailResponse.builder()
        .id(report.getId())
        .reportType(report.getReportType())
        .targetId(report.getTargetId())
        .reasonCode(report.getReportReasonType().name())
        .reasonDisplayName(report.getReportReasonType().getDisplayName())
        .reporter(report.getReporter().getNickname())
        .reportedUser(report.getReportedUser().getNickname())
        .reportReasonType(report.getReportReasonType())
        .status(report.getStatus())
        .reportedAt(report.getReportedAt())
        .build();
  }
}
