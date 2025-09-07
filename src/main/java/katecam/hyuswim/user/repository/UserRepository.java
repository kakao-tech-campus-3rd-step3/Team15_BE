package katecam.hyuswim.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
}
