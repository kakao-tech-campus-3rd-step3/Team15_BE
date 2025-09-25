package katecam.hyuswim.auth.service;

import katecam.hyuswim.auth.domain.RefreshToken;
import katecam.hyuswim.auth.dto.LoginTokens;
import katecam.hyuswim.auth.util.JwtUtil;
import katecam.hyuswim.auth.repository.RefreshTokenRepository;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static katecam.hyuswim.common.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken save(User user, String token, LocalDateTime expiryDate) {
        RefreshToken refreshToken = new RefreshToken(user, token, expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(INVALID_REFRESH_TOKEN));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new CustomException(EXPIRED_REFRESH_TOKEN);
        }
        return refreshToken;
    }

    @Transactional
    public LoginTokens refresh(String refreshTokenValue) {
        RefreshToken refreshToken = validateToken(refreshTokenValue);

        User user = userRepository.findById(refreshToken.getUser().getId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());

        String newRefreshToken = refreshTokenValue;

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now().plusDays(1))) {
            newRefreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getRole());
            refreshTokenRepository.delete(refreshToken);
            save(user, newRefreshToken, LocalDateTime.now().plusDays(7));
        }

        return new LoginTokens(newAccessToken, newRefreshToken);
    }
}
