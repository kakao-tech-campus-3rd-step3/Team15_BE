package katecam.hyuswim.user.service;

import java.util.Optional;

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

  @Transactional
  public void saveUser(SignupRequest signupRequest) {

    String encPassword = bCryptPasswordEncoder.encode(signupRequest.getPassword());

    userRepository.save(
        new User(signupRequest.getEmail(), encPassword, signupRequest.getNickname()));
  }

  @Transactional
  public boolean existUser(LoginRequest loginRequest) {
    Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

    if (userOptional.isEmpty()) {
      return false;
    }

    User user = userOptional.get();
    return bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword());
  }

  @Transactional
  public User findUserByEmail(String email) throws UserNotFoundException {
    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isEmpty()) {
      throw new UserNotFoundException("해당 유저는 존재하지 않습니다.");
    }
    return userOptional.get();
  }
}
