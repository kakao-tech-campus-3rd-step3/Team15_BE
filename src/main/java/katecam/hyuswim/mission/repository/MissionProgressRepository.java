package katecam.hyuswim.mission.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.mission.progress.MissionProgress;

public interface MissionProgressRepository extends JpaRepository<MissionProgress, Long> {
  long countByUserIdAndProgressDate(Long userId, LocalDate date);

  Optional<MissionProgress> findFirstByUserIdAndProgressDate(Long userId, LocalDate date);

  Optional<MissionProgress> findFirstByUserIdAndMissionIdAndProgressDate(
      Long userId, Long missionId, LocalDate date);

  long countByMissionIdAndProgressDate(Long missionId, LocalDate date);

  long countByMissionIdAndProgressDateAndIsCompletedTrue(Long missionId, LocalDate date);
}
