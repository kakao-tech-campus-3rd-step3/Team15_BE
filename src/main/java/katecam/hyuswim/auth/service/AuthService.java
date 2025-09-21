package katecam.hyuswim.auth.service;

import katecam.hyuswim.auth.dto.LoginRequest;
import katecam.hyuswim.auth.dto.SignupRequest;
import katecam.hyuswim.auth.jwt.JwtUtil;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.domain.UserStatus;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(SignupRequest request) {
        String email = request.getEmail();
        AuthProvider provider = AuthProvider.LOCAL;

        boolean bannedExists = userRepository.existsByEmailAndProviderAndStatus(
                email, provider, UserStatus.BANNED
        );

        if (bannedExists) {
            throw new CustomException(ErrorCode.REJOIN_NOT_ALLOWED);
        }

        String encPassword = bCryptPasswordEncoder.encode(request.getPassword());

        userRepository.save(new User(email, encPassword, provider));
    }

    @Transactional
    public String login(LoginRequest request) {
        User user = userRepository.findByEmailAndProviderAndIsDeletedFalse(
                        request.getEmail(), AuthProvider.LOCAL)
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        return jwtUtil.generateAccessToken(user.getId(), user.getRole());
    }
}
