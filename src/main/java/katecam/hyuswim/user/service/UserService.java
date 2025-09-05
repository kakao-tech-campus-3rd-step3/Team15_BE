package katecam.hyuswim.user.service;

import katecam.hyuswim.user.User;
import katecam.hyuswim.user.dto.LoginRequest;
import katecam.hyuswim.user.dto.SignupRequest;
import katecam.hyuswim.user.exception.UserNotFoundException;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional
    public boolean existUser(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        return bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword());
    }

    @Transactional
    public User findUserByUsername(String username) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("해당 유저는 존재하지 않습니다.");
        }
        return userOptional.get();
    }

}
