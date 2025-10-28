package katecam.hyuswim.badge.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import katecam.hyuswim.badge.domain.*;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByKindOrderByThresholdAsc(BadgeKind kind);
}
