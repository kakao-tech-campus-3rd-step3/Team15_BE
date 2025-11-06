package katecam.hyuswim.counseling.repository;

import katecam.hyuswim.counseling.domain.CounselingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselingSessionRepository extends JpaRepository<CounselingSession, String> {
}
