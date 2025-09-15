package katecam.hyuswim.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.mission.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {}
