// package katecam.hyuswim.mission.service;
package katecam.hyuswim.mission.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.mission.dto.MissionTodayDto;
import katecam.hyuswim.mission.progress.MissionProgress;
import katecam.hyuswim.mission.repository.MissionRepository;

@Service
@Transactional(readOnly = true)
public class MissionQueryService {

    private final MissionRepository missionRepo;

    @PersistenceContext private EntityManager em;

    public MissionQueryService(MissionRepository missionRepo) {
        this.missionRepo = missionRepo;
    }

    /**
     * 오늘 날짜 기준으로 모든 미션을 가져오고,
     * 각 미션의 오늘 통계(started/completed)와
     * 특정 사용자(userId)의 오늘 상태를 함께 얹어 반환.
     */
    public List<MissionTodayDto> getTodayMissionsWithState(Long userId) {
        LocalDate today = LocalDate.now();

        // 모든 미션(활성/비활성 포함) 가져오고 화면에서 active만 노출해도 됨
        List<Mission> all = missionRepo.findAll();

        // 사용자 오늘 진행 중인 레코드(있으면 하루 1건) 미리 조회
        MissionProgress myToday =
                em.createQuery(
                                "select p from MissionProgress p " +
                                        "where p.user.id = :uid and p.progressDate = :d", MissionProgress.class)
                        .setParameter("uid", userId)
                        .setParameter("d", today)
                        .getResultStream().findFirst().orElse(null);

        // 각 미션별 통계와 사용자 상태 매핑
        return all.stream().map(m -> {
            MissionTodayDto dto = MissionTodayDto.of(m);

            Long started = em.createQuery(
                            "select count(p) from MissionProgress p " +
                                    "where p.mission.id = :mid and p.progressDate = :d", Long.class)
                    .setParameter("mid", m.getId())
                    .setParameter("d", today)
                    .getSingleResult();

            Long completed = em.createQuery(
                            "select count(p) from MissionProgress p " +
                                    "where p.mission.id = :mid and p.progressDate = :d and p.isCompleted = true", Long.class)
                    .setParameter("mid", m.getId())
                    .setParameter("d", today)
                    .getSingleResult();

            dto.startedCount = started;
            dto.completedCount = completed;

            if (myToday == null) {
                dto.state = MissionTodayDto.TodayState.NOT_STARTED;
            } else if (!myToday.getIsCompleted() && myToday.getMission().getId().equals(m.getId())) {
                dto.state = MissionTodayDto.TodayState.IN_PROGRESS;
            } else if (myToday.getIsCompleted() && myToday.getMission().getId().equals(m.getId())) {
                dto.state = MissionTodayDto.TodayState.COMPLETED;
            } else {
                // 오늘 다른 미션을 이미 시작/완료했으면, 이 미션은 오늘 시작 불가
                dto.state = MissionTodayDto.TodayState.NOT_STARTED;
            }

            return dto;
        }).toList();
    }
}
