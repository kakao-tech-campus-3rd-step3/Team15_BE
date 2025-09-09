package katecam.hyuswim.user.service;

import java.util.Optional;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.common.jwt.JwtUtil;
import org.apache.catalina.webresources.ExtractingRoot;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.user.User;
import katecam.hyuswim.user.dto.LoginRequest;
import katecam.hyuswim.user.dto.SignupRequest;
import katecam.hyuswim.user.exception.UserNotFoundException;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
  private final JwtUtil jwtUtil;


  @Transactional
  public void saveUser(SignupRequest signupRequest) {

    String encPassword = bCryptPasswordEncoder.encode(signupRequest.getPassword());

    userRepository.save(
        new User(signupRequest.getEmail(), encPassword, signupRequest.getNickname()));
  }

  @Transactional
  public String login(LoginRequest loginRequest) {
    Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

    if(userOptional.isEmpty()) {
      throw new CustomException(ErrorCode.LOGIN_FALSE);
    }
    User user = userOptional.get();
    if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new CustomException(ErrorCode.LOGIN_FALSE);
    }

    return jwtUtil.generateToken(user.getEmail(), user.getRole());

  }

}
