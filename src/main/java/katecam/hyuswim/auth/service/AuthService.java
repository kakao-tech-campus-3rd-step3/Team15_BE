package katecam.hyuswim.auth.service;

import katecam.hyuswim.auth.dto.*;
import katecam.hyuswim.auth.util.CookieUtil;
import katecam.hyuswim.auth.util.JwtUtil;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.domain.UserStatus;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Transactional
    public User signup(SignupRequest request) {
        String email = request.getEmail();
        AuthProvider provider = AuthProvider.LOCAL;

        userRepository.findByEmailAndProvider(email, provider)
                .ifPresent(this::validateReSignup);;

        return registerUser(request, provider);
    }

    private void validateReSignup(User user) {
        if (user.getStatus() == UserStatus.BANNED) {
            throw new CustomException(ErrorCode.REJOIN_NOT_ALLOWED);
        }
        if (!user.getIsDeleted()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private User registerUser(SignupRequest request, AuthProvider provider) {
        String encPassword = bCryptPasswordEncoder.encode(request.getPassword());
        return userRepository.save(new User(request.getEmail(), encPassword, provider));
    }


    @Transactional
    public LoginTokens login(LoginRequest request) {
        User user = userRepository.findByEmailAndProviderAndIsDeletedFalse(
                        request.getEmail(), AuthProvider.LOCAL)
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getRole());

        refreshTokenService.save(user, refreshToken, LocalDateTime.now().plusDays(7));

        return new LoginTokens(accessToken, refreshToken);
    }
}
