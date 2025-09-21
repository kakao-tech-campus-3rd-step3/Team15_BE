package katecam.hyuswim.user.repository;

import java.util.Optional;

import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.UserRole;
import katecam.hyuswim.user.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndProvider(String email, AuthProvider provider);
    Optional<User> findByEmailAndProviderAndIsDeletedFalse(String email, AuthProvider provider);
    Optional<User> findByProviderAndProviderId(AuthProvider provider, Long providerId);
    Optional<User> findByRole(UserRole role);
    boolean existsByEmailAndProviderAndStatus(String email, AuthProvider provider, UserStatus status);

}
