package katecam.hyuswim.counseling.service;

import katecam.hyuswim.counseling.domain.CounselingSession;
import katecam.hyuswim.counseling.domain.CounselingStep;
import katecam.hyuswim.counseling.repository.CounselingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CounselingSessionScheduler {

    private final CounselingSessionRepository sessionRepository;

    // 매일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 * * *")
    public void closeExpiredSessions() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(20);

        List<CounselingSession> expiredSessions =
                sessionRepository.findByStepAndLastMessageAtBefore(CounselingStep.ACTIVE, threshold);

        expiredSessions.forEach(CounselingSession::closeIfInactive);

        sessionRepository.saveAll(expiredSessions);
    }
}
