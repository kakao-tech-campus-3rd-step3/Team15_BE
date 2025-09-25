package katecam.hyuswim.ai.service;

import jakarta.annotation.PostConstruct;
import katecam.hyuswim.auth.domain.UserAuth;
import katecam.hyuswim.auth.repository.UserAuthRepository;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.domain.UserRole;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
public class AiUserService {
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private User aiUser;

    @PostConstruct
    public void init() {
        this.aiUser = userRepository.findByRole(UserRole.AI)
                .orElseGet(() -> {
                    // 1. User 생성
                    User newAiUser = new User(
                            "푸르미",
                            "늘 곁에서 응원해주는 작은 새싹 친구예요",
                            UserRole.AI
                    );
                    userRepository.save(newAiUser);

                    UserAuth aiAuth = new UserAuth(newAiUser, AuthProvider.LOCAL);
                    userAuthRepository.save(aiAuth);

                    return newAiUser;
                });
    }
}
