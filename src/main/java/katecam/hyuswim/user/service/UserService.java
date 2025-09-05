package katecam.hyuswim.user.service;

import katecam.hyuswim.user.dto.SignupRequest;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();



    @Transactional
    public void saveUser(SignupRequest signupRequest) {

        String encPassword = bCryptPasswordEncoder.encode(signupRequest.getPassword());
        signupRequest.setPassword(encPassword);

        userRepository.save(signupRequest.toEntity());

    }
}
