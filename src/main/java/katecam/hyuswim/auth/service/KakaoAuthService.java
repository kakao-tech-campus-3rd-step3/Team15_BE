package katecam.hyuswim.auth.service;

import katecam.hyuswim.auth.client.KakaoClient;
import katecam.hyuswim.auth.config.KakaoProperties;
import katecam.hyuswim.auth.domain.UserAuth;
import katecam.hyuswim.auth.dto.KakaoTokenResponse;
import katecam.hyuswim.auth.dto.KakaoUserResponse;
import katecam.hyuswim.auth.dto.LoginTokens;
import katecam.hyuswim.auth.repository.UserAuthRepository;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import katecam.hyuswim.auth.util.JwtUtil;

import java.net.URI;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final KakaoClient kakaoClient;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public String generateLoginUrl() {
        return UriComponentsBuilder.newInstance()
                .uri(URI.create(kakaoProperties.authorizeUri()))
                .queryParam("client_id", kakaoProperties.clientId())
                .queryParam("redirect_uri", kakaoProperties.redirectUri())
                .queryParam("response_type", "code")
                .toUriString();
    }

    @Transactional
    public LoginTokens loginWithKakao(String code) {

        KakaoTokenResponse tokenResponse = kakaoClient.getToken(code);
        KakaoUserResponse userResponse = kakaoClient.getUser(tokenResponse.access_token());

        UserAuth userAuth = userAuthRepository.findByProviderAndProviderIdAndUser_IsDeletedFalse(AuthProvider.KAKAO, String.valueOf(userResponse.id()))
                .orElseGet(() -> createKakaoUserAuth(userResponse));

        User user = userAuth.getUser();

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getRole());

        refreshTokenService.save(user, refreshToken, LocalDateTime.now().plusDays(7));

        return new LoginTokens(accessToken, refreshToken);
    }

    private UserAuth createKakaoUserAuth(KakaoUserResponse userResponse) {
        User newUser = User.createDefault();
        userRepository.save(newUser);

        UserAuth newAuth = UserAuth.createKakao(newUser, String.valueOf(userResponse.id()));
        userAuthRepository.save(newAuth);

        return newAuth;
    }
}
