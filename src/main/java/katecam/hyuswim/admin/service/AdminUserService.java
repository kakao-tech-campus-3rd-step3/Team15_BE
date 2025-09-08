package katecam.hyuswim.admin.service;

import katecam.hyuswim.user.User;
import katecam.hyuswim.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminUserService {
    private final UserRepository userRepository;
    public AdminUserService(UserRepository userRepository) { this.userRepository = userRepository; }
    public List<User> findAll() { return userRepository.findAll(); }
}
