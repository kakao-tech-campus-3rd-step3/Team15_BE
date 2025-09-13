package katecam.hyuswim.report.dto;

import katecam.hyuswim.report.domain.ReportReasonType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportReasonResponse {
  private String reportReasonType;
  private String displayName;

  public static ReportReasonResponse from(ReportReasonType type) {
    return new ReportReasonResponse(type.name(), type.getDisplayName());
  }
}
