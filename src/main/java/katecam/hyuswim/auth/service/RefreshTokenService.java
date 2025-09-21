package katecam.hyuswim.auth.service;

import katecam.hyuswim.auth.domain.RefreshToken;
import katecam.hyuswim.auth.repository.RefreshTokenRepository;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static katecam.hyuswim.common.error.ErrorCode.EXPIRED_REFRESH_TOKEN;
import static katecam.hyuswim.common.error.ErrorCode.INVALID_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(User user, String token, LocalDateTime expiryDate) {
        RefreshToken refreshToken = new RefreshToken(user, token, expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(INVALID_REFRESH_TOKEN));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(EXPIRED_REFRESH_TOKEN);
        }
        return refreshToken;
    }
}
