package katecam.hyuswim.user.repository;

import java.util.List;
import java.util.Optional;

import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.UserRole;
import katecam.hyuswim.user.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByRole(UserRole role);
    List<User> findByRoleNot(UserRole role);
}
