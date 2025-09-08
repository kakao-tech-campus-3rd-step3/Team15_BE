package katecam.hyuswim.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.mission.progress.MissionProgress;

public interface MissionProgressRepository extends JpaRepository<MissionProgress, Long> {}
