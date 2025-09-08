package katecam.hyuswim.mission.repository;

import katecam.hyuswim.mission.progress.MissionProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MissionProgressRepository extends JpaRepository<MissionProgress, Long> {

}
