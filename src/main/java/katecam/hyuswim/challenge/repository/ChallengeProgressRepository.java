package katecam.hyuswim.challenge.repository;

import java.util.List;
import java.util.Optional;

import katecam.hyuswim.challenge.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import katecam.hyuswim.challenge.domain.ChallengeProgress;
import katecam.hyuswim.user.domain.User;

public interface ChallengeProgressRepository extends JpaRepository<ChallengeProgress, Long> {
    Optional<ChallengeProgress> findByUserAndChallenge(User user, Challenge challenge);
    List<ChallengeProgress> findByUser(User user);
}

