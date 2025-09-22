package katecam.hyuswim.auth.repository;

import katecam.hyuswim.auth.domain.UserAuth;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    Optional<UserAuth> findByEmailAndProvider(String email, AuthProvider provider);
    Optional<UserAuth> findByUserAndProvider(User user, AuthProvider provider);
    Optional<UserAuth> findByEmailAndProviderAndUser_IsDeletedFalse(String email, AuthProvider provider);
    Optional<UserAuth> findByProviderAndProviderIdAndUser_IsDeletedFalse(AuthProvider provider, String providerId);
}
