package katecam.hyuswim.report.dto;

import java.time.LocalDateTime;

import katecam.hyuswim.report.domain.ReasonType;
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
public class ReportListResponse {
  private Long id;
  private ReportType reportType;
  private Long targetId;
  private String reasonCode;
  private String reasonDisplayName;
  private String content;
  private String reporter;
  private String reportedUser;
  private ReasonType reasonType;
  private ReportStatus status;
  private LocalDateTime reportedAt;

  public static ReportListResponse from(Report report) {
    return ReportListResponse.builder()
        .id(report.getId())
        .reportType(report.getReportType())
        .targetId(report.getTargetId())
        .reasonCode(report.getReasonType().name())
        .reasonDisplayName(report.getReasonType().getDisplayName())
        .reporter(report.getReporter().getNickname())
        .reportedUser(report.getReportedUser().getNickname())
        .reasonType(report.getReasonType())
        .status(report.getStatus())
        .reportedAt(report.getReportedAt())
        .build();
  }
}
