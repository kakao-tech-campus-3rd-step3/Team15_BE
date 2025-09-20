package katecam.hyuswim.challenge.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.challenge.ChallengeRuleType;
import katecam.hyuswim.challenge.domain.Challenge;
import katecam.hyuswim.challenge.domain.ChallengeProgress;
import katecam.hyuswim.challenge.repository.ChallengeProgressRepository;
import katecam.hyuswim.challenge.repository.ChallengeRepository;
import katecam.hyuswim.mission.Mission;
import katecam.hyuswim.mission.repository.MissionProgressRepository;
import katecam.hyuswim.mission.repository.MissionRepository;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeProgressRepository challengeProgressRepository;
    private final MissionRepository missionRepository;
    private final MissionProgressRepository missionProgressRepository;
    private final UserRepository userRepository;

    public void onMissionCompleted(Long userId, Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        LocalDateTime now = LocalDateTime.now();

        List<Challenge> challenges = challengeRepository.findByActiveTrue();

        for (Challenge ch : challenges) {
            if (!matches(ch, mission, now)) continue;

            ChallengeProgress cp = challengeProgressRepository
                    .findByUserIdAndChallengeId(userId, ch.getId())
                    .orElseGet(() -> ChallengeProgress.startOf(user, ch));

            if (cp.isAchieved()) continue;

            long current = computeCurrentValue(userId, ch);
            cp.updateCurrent(current);

            if (current >= ch.getTargetValue()) {
                cp.achieve(now);

                if (ch.getRewardPoints() > 0) {
                    user.addPoints(ch.getRewardPoints());
                    userRepository.save(user);
                }
            }

            challengeProgressRepository.save(cp);
        }
    }

    private boolean matches(Challenge ch, Mission mission, LocalDateTime now) {
        if (ch.getRequiredCategory() != null && mission.getCategory() != ch.getRequiredCategory()) return false;
        if (ch.getRequiredLevel() != null && mission.getLevel() != ch.getRequiredLevel()) return false;
        if (ch.getTimeWindowStart() != null && now.isBefore(ch.getTimeWindowStart())) return false;
        if (ch.getTimeWindowEnd() != null && !now.isBefore(ch.getTimeWindowEnd())) return false;
        return true;
    }

    private long computeCurrentValue(Long userId, Challenge ch) {
        ChallengeRuleType t = ch.getRuleType();
        return switch (t) {
            case COUNT_BY_LEVEL       -> missionProgressRepository.countCompletedByUserAndLevel(userId, ch.getRequiredLevel());
            case COUNT_BY_CATEGORY    -> missionProgressRepository.countCompletedByUserAndCategory(userId, ch.getRequiredCategory());
            case TOTAL_COMPLETED      -> missionProgressRepository.countCompletedByUser(userId);
            case POINTS_SUM           -> missionProgressRepository.sumCompletedPointsByUser(userId);
        };
    }

    // 조회용
    @Transactional(readOnly = true)
    public List<Challenge> listActive() {
        return challengeRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<ChallengeProgress> myProgress(Long userId) {
        return challengeProgressRepository.findByUserId(userId);
    }

    public void claimReward(Long userId, Long challengeId) {
        ChallengeProgress cp = challengeProgressRepository
                .findByUserIdAndChallengeId(userId, challengeId)
                .orElseThrow();

        if (!cp.isAchieved() || cp.isClaimed()) return;

        cp.claim(LocalDateTime.now());
        challengeProgressRepository.save(cp);
    }
}
