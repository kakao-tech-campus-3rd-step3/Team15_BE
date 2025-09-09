package katecam.hyuswim.report;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import katecam.hyuswim.user.User;

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
  @JoinColumn(name = "from_user_id")
  private User fromUser;

  @ManyToOne
  @JoinColumn(name = "to_user_id")
  private User toUser;

  @Enumerated(EnumType.STRING)
  private ReasonType reasonType;

  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReportStatus status = ReportStatus.PENDING;

  @CreatedDate
  @Column(name = "reported_at", updatable = false)
  private LocalDateTime reportedAt;
}
