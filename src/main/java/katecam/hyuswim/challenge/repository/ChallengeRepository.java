package katecam.hyuswim.challenge.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import katecam.hyuswim.challenge.domain.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByActiveTrue();
    Optional<Challenge> findByCode(String code);
}
