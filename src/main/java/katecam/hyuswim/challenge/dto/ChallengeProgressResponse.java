package katecam.hyuswim.challenge.dto;

import java.time.LocalDateTime;
import katecam.hyuswim.challenge.domain.ChallengeProgress;

public record ChallengeProgressResponse(
        Long challengeId,
        long currentValue,
        boolean achieved,
        LocalDateTime achievedAt,
        boolean claimed,
        LocalDateTime claimedAt
) {
    public static ChallengeProgressResponse of(ChallengeProgress cp) {
        return new ChallengeProgressResponse(
                cp.getChallenge().getId(),
                cp.getCurrentValue(),
                cp.isAchieved(),
                cp.getAchievedAt(),
                cp.isClaimed(),
                cp.getClaimedAt()
        );
    }
}
