package katecam.hyuswim.mission.repository;

import java.time.LocalDate;
import java.util.Optional;

import katecam.hyuswim.mission.MissionCategory;
import katecam.hyuswim.mission.MissionLevel;
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

    @Query("""
  select count(mp) from MissionProgress mp
  where mp.user.id = :userId and mp.isCompleted = true
    and mp.mission.level = :level
""")
    long countCompletedByUserAndLevel(@Param("userId") Long userId,
                                      @Param("level") MissionLevel level);

    @Query("""
  select count(mp) from MissionProgress mp
  where mp.user.id = :userId and mp.isCompleted = true
    and mp.mission.category = :category
""")
    long countCompletedByUserAndCategory(@Param("userId") Long userId,
                                         @Param("category") MissionCategory category);

    @Query("""
  select count(mp) from MissionProgress mp
  where mp.user.id = :userId and mp.isCompleted = true
""")
    long countCompletedByUser(@Param("userId") Long userId);

    @Query("select count(distinct m.progressDate) from MissionProgress m where m.user.id = :userId")
    long countDistinctDaysByUserId(Long userId);
}
