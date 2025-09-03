package katecam.hyuswim.report;

import jakarta.persistence.*;
import katecam.hyuswim.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    private String content;

    @Enumerated(EnumType.STRING)
    private ReasonType reasonType;

    @CreatedDate
    @Column(name = "reported_at", updatable = false)
    private LocalDateTime reportedAt;  // 신고 시간

    private Boolean isProcessed;  // 처리 완료 여부
}
