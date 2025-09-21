package katecam.hyuswim.user.service;

import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.UserStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.auth.jwt.JwtUtil;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.dto.LoginRequest;
import katecam.hyuswim.user.dto.SignupRequest;
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
        String email = signupRequest.getEmail();
        AuthProvider provider = AuthProvider.LOCAL;

        boolean bannedExists = userRepository.existsByEmailAndProviderAndStatus(
                email, provider, UserStatus.BANNED
        );
        if (bannedExists) {
            throw new CustomException(ErrorCode.REJOIN_NOT_ALLOWED);
        }

        String encPassword = bCryptPasswordEncoder.encode(signupRequest.getPassword());
        userRepository.save(new User(email, encPassword, provider));
    }


    @Transactional
    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmailAndProviderAndIsDeletedFalse(
                        loginRequest.getEmail(), AuthProvider.LOCAL)
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        return jwtUtil.generateAccessToken(user.getId(), user.getRole());
    }

    @Transactional
    public void deleteUser(User user, String confirmText) {
      if (!confirmText.equals("계정 탈퇴")){
          throw new CustomException(ErrorCode.WITHDRAWAL_FAILED);
      }
      user.delete();
    }
}
