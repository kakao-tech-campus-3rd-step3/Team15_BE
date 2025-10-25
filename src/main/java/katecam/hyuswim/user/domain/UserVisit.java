package katecam.hyuswim.user.domain;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "user_visits",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_visit_oneday", columnNames = {"user_id","visit_date"}))
@Getter @NoArgsConstructor
public class UserVisit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name="visit_date", nullable=false)
    private LocalDate visitDate;

    public UserVisit(User user, LocalDate visitDate) {
        this.user = user;
        this.visitDate = visitDate;
    }
}

