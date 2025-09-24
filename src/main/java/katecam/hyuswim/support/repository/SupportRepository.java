package katecam.hyuswim.support.repository;

import katecam.hyuswim.support.domain.Support;
import katecam.hyuswim.support.domain.SupportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {

    List<Support> findBySupportType(SupportType supportType);
    @Query("SELECT COUNT(s) FROM Support s WHERE s.endPoint >= CURRENT_DATE")
    long countActiveSupports();
}
