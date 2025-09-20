package katecam.hyuswim.challenge.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import katecam.hyuswim.challenge.domain.ChallengeProgress;

public interface ChallengeProgressRepository extends JpaRepository<ChallengeProgress, Long> {
    Optional<ChallengeProgress> findByUserIdAndChallengeId(Long userId, Long challengeId);
    List<ChallengeProgress> findByUserId(Long userId);
}

