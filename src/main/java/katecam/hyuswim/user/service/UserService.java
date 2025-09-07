package katecam.hyuswim.user.service;

import katecam.hyuswim.jwt.JwtUtil;
import katecam.hyuswim.jwt.dto.JwtTokenResponse;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.dto.LoginRequest;
import katecam.hyuswim.user.dto.SignupRequest;
import katecam.hyuswim.user.exception.LoginFalseException;
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
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();



    @Transactional
    public void saveUser(SignupRequest signupRequest) {

        String encPassword = bCryptPasswordEncoder.encode(signupRequest.getPassword());

        userRepository.save(new User(signupRequest.getEmail(), encPassword, signupRequest.getNickname()));

    }

    @Transactional
    public String login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("해당 이메일을 가진 유저를 찾을 수 없습니다");
        }
        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new LoginFalseException("비밀번호가 일치하지 않습니다.");
        }
        return jwtUtil.generateToken(user.getEmail(), user.getRole());

    }

}
