package katecam.hyuswim.mission.service;

import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.mission.progress.MissionProgress;
import katecam.hyuswim.mission.repository.MissionRepository;
import katecam.hyuswim.mission.repository.MissionProgressRepository;
import katecam.hyuswim.user.UserRepository; // 실제 경로에 맞게
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
public class MissionService {

    private final MissionRepository missionRepo;
    private final MissionProgressRepository progressRepo;
    private final UserRepository userRepo;

    public MissionService(MissionRepository missionRepo,
                          MissionProgressRepository progressRepo,
                          UserRepository userRepo) {
        this.missionRepo = missionRepo;
        this.progressRepo = progressRepo;
        this.userRepo = userRepo;
    }

    public void startMission(Long userId, Long missionId) {
        var user = userRepo.findById(userId).orElseThrow();
        var mission = missionRepo.findById(missionId).orElseThrow();

        if (!mission.isActive()) throw new IllegalStateException("비활성 미션");

        LocalDate today = LocalDate.now();
        if (progressRepo.existsByUser_IdAndProgressDate(userId, today)) {
            throw new IllegalStateException("하루 1회 제한");
        }

        MissionProgress p = new MissionProgress();
        p.setUser(user);
        p.setMission(mission);
        p.setProgressDate(today);
        p.setStartedAt(LocalDateTime.now());
        p.setIsCompleted(false);
        progressRepo.save(p);
    }

    public void completeMission(Long userId, Long missionId) {
        LocalDate today = LocalDate.now();
        var prog = progressRepo.findByUser_IdAndMission_IdAndProgressDate(userId, missionId, today)
                .orElseThrow(() -> new IllegalStateException("오늘 시작 기록 없음"));

        if (Boolean.TRUE.equals(prog.getIsCompleted())) {
            throw new IllegalStateException("이미 완료");
        }

        prog.setIsCompleted(true);
        prog.setCompletedAt(LocalDateTime.now());

        // 포인트 지급 로직(필요 시)
    }
}
