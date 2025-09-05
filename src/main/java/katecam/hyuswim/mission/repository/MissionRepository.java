package katecam.hyuswim.mission.progress;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MissionProgressRepository extends JpaRepository<MissionProgress, Long> {

    boolean existsByUser_IdAndProgressDate(Long userId, LocalDate date);

    Optional<MissionProgress> findByUser_IdAndMission_IdAndProgressDate(Long userId, Long missionId, LocalDate date);

    long countByMission_IdAndProgressDate(Long missionId, LocalDate date); // 오늘 시작(혹은 진행중 포함)

    long countByMission_IdAndProgressDateAndIsCompletedTrue(Long missionId, LocalDate date); // 오늘 완료 수
}
