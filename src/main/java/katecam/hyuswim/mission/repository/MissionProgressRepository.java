package katecam.hyuswim.mission.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import katecam.hyuswim.mission.progress.MissionProgress;

public interface MissionProgressRepository extends JpaRepository<MissionProgress, Long> {
  long countByUserIdAndProgressDate(Long userId, LocalDate date);

  Optional<MissionProgress> findFirstByUserIdAndProgressDate(Long userId, LocalDate date);

  Optional<MissionProgress> findFirstByUserIdAndMissionIdAndProgressDate(
      Long userId, Long missionId, LocalDate date);

  long countByMissionIdAndProgressDate(Long missionId, LocalDate date);

  long countByMissionIdAndProgressDateAndIsCompletedTrue(Long missionId, LocalDate date);

  long countByUserIdAndIsCompletedTrue(Long userId);

  @Query(
      """
    select coalesce(sum(mp.mission.point), 0)
    from MissionProgress mp
    where mp.user.id = :userId and mp.isCompleted = true
  """)
  Long sumCompletedPointsByUser(@Param("userId") Long userId);

  long countByUserIdAndProgressDateAndIsCompletedFalse(Long userId, LocalDate date);
}
