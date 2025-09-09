package katecam.hyuswim.report.dto;

import katecam.hyuswim.report.domain.ReasonType;
import katecam.hyuswim.report.domain.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    private ReportType reportType;
    private Long targetId;
    private ReasonType reasonType;
    private String content;
}
