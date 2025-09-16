package katecam.hyuswim.report.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ReportType reportType;

  @Column(nullable = false)
  private Long targetId;

  @ManyToOne
  @JoinColumn(name = "reporter_id")
  private User reporter;

  @ManyToOne
  @JoinColumn(name = "reported_user_id")
  private User reportedUser;

  @Enumerated(EnumType.STRING)
  private ReportReasonType reportReasonType;

  @Column(length = 500)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReportStatus status = ReportStatus.PENDING;

  @CreatedDate
  @Column(name = "reported_at", updatable = false)
  private LocalDateTime reportedAt;

  private Report(
      User reporter,
      User reportedUser,
      ReportType reportType,
      Long targetId,
      ReportReasonType reportReasonType,
      String content) {
    this.reporter = reporter;
    this.reportedUser = reportedUser;
    this.reportType = reportType;
    this.targetId = targetId;
    this.reportReasonType = reportReasonType;
    this.content = content;
  }

  public static Report create(
      User reporter,
      User reportedUser,
      ReportType reportType,
      Long targetId,
      ReportReasonType reportReasonType,
      String content) {

    if (reportReasonType == ReportReasonType.OTHER && (content == null || content.isBlank())) {
      throw new CustomException(ErrorCode.REPORT_REASON_REQUIRED);
    }

    return new Report(reporter, reportedUser, reportType, targetId, reportReasonType, content);
  }
  public void updateStatus(ReportStatus status) {
      this.status = status;
  }
}
