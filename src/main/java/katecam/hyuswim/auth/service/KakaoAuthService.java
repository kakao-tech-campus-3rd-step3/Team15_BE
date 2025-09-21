package katecam.hyuswim.auth.service;

import katecam.hyuswim.auth.client.KakaoClient;
import katecam.hyuswim.auth.config.KakaoProperties;
import katecam.hyuswim.auth.dto.AuthResponseDTO;
import katecam.hyuswim.auth.dto.KakaoTokenResponse;
import katecam.hyuswim.auth.dto.KakaoUserResponse;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import katecam.hyuswim.auth.jwt.JwtUtil;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;
    private final UserRepository userRepository;
    private final KakaoClient kakaoClient;
    private final JwtUtil jwtUtil;

    public String generateLoginUrl() {
        return UriComponentsBuilder.newInstance()
                .uri(URI.create(kakaoProperties.authorizeUri()))
                .queryParam("client_id", kakaoProperties.clientId())
                .queryParam("redirect_uri", kakaoProperties.redirectUri())
                .queryParam("response_type", "code")
                .toUriString();
    }

    @Transactional
    public AuthResponseDTO loginWithKakao(String code) {
        KakaoTokenResponse tokenResponse = kakaoClient.getToken(code);
        KakaoUserResponse userResponse = kakaoClient.getUser(tokenResponse.access_token());

        User user = userRepository.findByProviderAndProviderId(AuthProvider.KAKAO, userResponse.id())
                .orElseGet(() -> userRepository.save(
                        User.createKakaoUser(AuthProvider.KAKAO, userResponse.id())
                ));

        String jwt = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        return new AuthResponseDTO(jwt);
    }
}
