package katecam.hyuswim.auth.service;

import katecam.hyuswim.auth.domain.UserAuth;
import katecam.hyuswim.auth.dto.*;
import katecam.hyuswim.auth.repository.UserAuthRepository;
import katecam.hyuswim.auth.util.CookieUtil;
import katecam.hyuswim.auth.util.JwtUtil;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.domain.UserStatus;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
//    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;

    @Transactional
    public User signup(SignupRequest request) {
        String email = request.getEmail();

//        String verified = redisTemplate.opsForValue().get("auth:email:verified:" + email);
//
//        if (!"true".equals(verified)) {
//            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
//        }

        userAuthRepository.findByEmailAndProvider(email, AuthProvider.LOCAL)
                .ifPresent(auth -> validateReSignup(auth.getUser()));

        User user = User.createDefault();
        userRepository.save(user);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserAuth auth = UserAuth.createLocal(user, request, encodedPassword);
        userAuthRepository.save(auth);

        return user;
    }

    private void validateReSignup(User user) {
        if (user.getStatus() == UserStatus.BANNED) {
            throw new CustomException(ErrorCode.REJOIN_NOT_ALLOWED);
        }
        if (!user.getIsDeleted()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public LoginTokens login(LoginRequest request) {
        UserAuth userAuth = userAuthRepository.findByEmailAndProviderAndUser_IsDeletedFalse(request.getEmail(), AuthProvider.LOCAL)
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), userAuth.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        User user = userAuth.getUser();

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getRole());

        refreshTokenService.save(user, refreshToken, LocalDateTime.now().plusDays(7));

        user.updateLastActiveDate();

        return new LoginTokens(accessToken, refreshToken);
    }
}
