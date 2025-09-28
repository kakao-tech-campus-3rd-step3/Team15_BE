package katecam.hyuswim.badge.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "badges",
        uniqueConstraints = @UniqueConstraint(name = "uk_badge_kind_tier", columnNames = {"kind", "tier"}))
@Getter
@NoArgsConstructor
public class Badge {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 40)
    private BadgeKind kind;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private BadgeTier tier;

    @Column(nullable = false)
    private String name;      // 노출명: “사랑 전도사 1단계” 등

    @Column(nullable = false)
    private String imageUrl;  // 단계별 이미지

    @Column(nullable = false)
    private int threshold;    // 기준치(1/7/15/30)

    public Badge(BadgeKind kind, BadgeTier tier, String name, String imageUrl) {
        this.kind = kind;
        this.tier = tier;
        this.name = name;
        this.imageUrl = imageUrl;
        this.threshold = tier.getThreshold();
    }
}
