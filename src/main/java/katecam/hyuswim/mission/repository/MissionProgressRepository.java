package katecam.hyuswim.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.mission.progress.MissionProgress;

import java.time.LocalDate;
import java.util.Optional;

public interface MissionProgressRepository extends JpaRepository<MissionProgress, Long> {
    long countByUser_IdAndProgressDate(Long userId, LocalDate date);

    Optional<MissionProgress> findFirstByUser_IdAndProgressDate(Long userId, LocalDate date);

    Optional<MissionProgress> findFirstByUser_IdAndMission_IdAndProgressDate(Long userId, Long missionId, LocalDate date);

    long countByMission_IdAndProgressDate(Long missionId, LocalDate date);

    long countByMission_IdAndProgressDateAndIsCompletedTrue(Long missionId, LocalDate date);
}
