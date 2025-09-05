package katecam.hyuswim.user.repository;

import katecam.hyuswim.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
