package katecam.hyuswim.support.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Support {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String company;

    @Enumerated(EnumType.STRING)
    private SupportType supportType;

    private String content;

    private String place;

    private LocalDate endPoint;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void update(String name, String company, String content, String place, LocalDate endPoint, SupportType type) {
        if (name != null) this.name = name;
        if (company != null) this.company = company;
        if (content != null) this.content = content;
        if (place != null) this.place = place;
        if (endPoint != null) this.endPoint = endPoint;
        if (type != null) this.supportType = type;
    }

}
