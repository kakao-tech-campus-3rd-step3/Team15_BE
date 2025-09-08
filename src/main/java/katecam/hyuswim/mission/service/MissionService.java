package katecam.hyuswim.mission.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.mission.progress.MissionProgress;
import katecam.hyuswim.mission.repository.MissionRepository;
import katecam.hyuswim.mission.repository.MissionProgressRepository;
import katecam.hyuswim.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
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

    @PersistenceContext
    private EntityManager em;

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

        if (!mission.isActive()) {
            throw new IllegalStateException("비활성 미션");
        }

        LocalDate today = LocalDate.now();

        //하루 1회 제한: JPQL로 중복 시작 여부 확인
        Long existsCnt = em.createQuery(
                        "select count(p) from MissionProgress p " +
                                "where p.user.id = :uid and p.progressDate = :d", Long.class)
                .setParameter("uid", userId)
                .setParameter("d", today)
                .getSingleResult();

        if (existsCnt > 0) {
            throw new IllegalStateException("하루 1회 제한");
        }

        // 진행 생성 & 저장
        MissionProgress p = new MissionProgress();
        p.setUser(user);
        p.setMission(mission);
        p.setProgressDate(today);
        p.setStartedAt(LocalDateTime.now());
        p.setIsCompleted(false);

        try {
            progressRepo.save(p);
        } catch (DataIntegrityViolationException e) {
            // 유니크 제약(uk_progress_user_date)과 race condition 대비
            throw new IllegalStateException("하루 1회 제한(동시 요청)", e);
        }
    }

    public void completeMission(Long userId, Long missionId) {
        LocalDate today = LocalDate.now();

        // 오늘 시작한 해당 미션 진행 레코드 조회 (JPQL)
        MissionProgress prog = em.createQuery(
                        "select p from MissionProgress p " +
                                "where p.user.id = :uid and p.mission.id = :mid and p.progressDate = :d", MissionProgress.class)
                .setParameter("uid", userId)
                .setParameter("mid", missionId)
                .setParameter("d", today)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("오늘 시작 기록 없음"));

        if (Boolean.TRUE.equals(prog.getIsCompleted())) {
            throw new IllegalStateException("이미 완료");
        }

        prog.setIsCompleted(true);
        prog.setCompletedAt(LocalDateTime.now());
    }

    // 집계 API용 헬퍼: 오늘 참여/완료 수
    public record MissionStats(long startedCount, long completedCount) {}
    public MissionStats getTodayStats(Long missionId) {
        LocalDate today = LocalDate.now();

        Long started = em.createQuery(
                        "select count(p) from MissionProgress p " +
                                "where p.mission.id = :mid and p.progressDate = :d", Long.class)
                .setParameter("mid", missionId)
                .setParameter("d", today)
                .getSingleResult();

        Long completed = em.createQuery(
                        "select count(p) from MissionProgress p " +
                                "where p.mission.id = :mid and p.progressDate = :d and p.isCompleted = true", Long.class)
                .setParameter("mid", missionId)
                .setParameter("d", today)
                .getSingleResult();

        return new MissionStats(started, completed);
    }
}
