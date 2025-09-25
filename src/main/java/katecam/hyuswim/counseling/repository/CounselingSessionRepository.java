package katecam.hyuswim.counseling.repository;

import katecam.hyuswim.counseling.domain.CounselingSession;
import katecam.hyuswim.counseling.domain.CounselingStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CounselingSessionRepository extends JpaRepository<CounselingSession, String> {
    List<CounselingSession> findByStepAndLastMessageAtBefore(CounselingStep step, LocalDateTime time);
}
