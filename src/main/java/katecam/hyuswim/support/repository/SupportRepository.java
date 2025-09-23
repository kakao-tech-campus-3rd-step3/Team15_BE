package katecam.hyuswim.support.repository;

import katecam.hyuswim.support.domain.Support;
import katecam.hyuswim.support.domain.SupportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {

    List<Support> findBySupportType(SupportType supportType);
}
