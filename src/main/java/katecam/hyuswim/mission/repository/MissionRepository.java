package katecam.hyuswim.mission.repository;

import katecam.hyuswim.mission.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}
