package katecam.hyuswim.user.service;

import jakarta.annotation.PostConstruct;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.UserRole;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiUserProvider {

    private final UserRepository userRepository;
    private User aiUser;

    @PostConstruct
    public void init(){
        this.aiUser = userRepository.findByRole(UserRole.AI)
                .orElseGet(() -> {
                    User newAiUser = new User(
                            "ai@email.com",
                            "푸르미",
                            "늘 곁에서 응원해주는 작은 새싹 친구예요",
                            UserRole.AI
                    );
                    return userRepository.save(newAiUser);
                });
    }

    public User getAiUser() {
        return aiUser;
    }

}
