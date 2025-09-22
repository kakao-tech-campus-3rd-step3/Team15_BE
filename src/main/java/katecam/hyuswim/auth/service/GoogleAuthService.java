package katecam.hyuswim.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import katecam.hyuswim.auth.config.GoogleProperties;
import katecam.hyuswim.auth.domain.UserAuth;
import katecam.hyuswim.auth.dto.GoogleIdTokenPayload;
import katecam.hyuswim.auth.dto.GoogleTokenResponse;
import katecam.hyuswim.auth.dto.LoginTokens;
import katecam.hyuswim.auth.repository.UserAuthRepository;
import katecam.hyuswim.auth.util.JwtUtil;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleProperties googleProperties;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateLoginUrl() {
        return UriComponentsBuilder.newInstance()
                .uri(URI.create(googleProperties.authorizeUri()))
                .queryParam("client_id", googleProperties.clientId())
                .queryParam("redirect_uri", googleProperties.redirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "openid")
                .toUriString();
    }

    @Transactional
    public LoginTokens loginWithGoogle(String code) {
        GoogleTokenResponse tokenResponse = requestToken(code);
        GoogleIdTokenPayload payload = extractPayload(tokenResponse.idToken());
        String sub = payload.sub();

        UserAuth userAuth = userAuthRepository.findByProviderAndProviderIdAndUser_IsDeletedFalse(AuthProvider.GOOGLE, sub)
                .orElseGet(() -> createGoogleUserAuth(sub));

        User user = userAuth.getUser();

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getRole());

        refreshTokenService.save(user, refreshToken, LocalDateTime.now().plusDays(7));

        return new LoginTokens(accessToken, refreshToken);
    }

    private GoogleTokenResponse requestToken(String code) {
        URI uri = URI.create(googleProperties.tokenUri());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", googleProperties.clientId());
        body.add("client_secret", googleProperties.clientSecret());
        body.add("redirect_uri", googleProperties.redirectUri());
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            return restTemplate.postForObject(uri, request, GoogleTokenResponse.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GOOGLE_TOKEN_REQUEST_FAILED);
        }
    }


    private GoogleIdTokenPayload extractPayload(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length < 2) {
                throw new CustomException(ErrorCode.GOOGLE_ID_TOKEN_INVALID);
            }
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            return objectMapper.readValue(payloadJson, GoogleIdTokenPayload.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GOOGLE_ID_TOKEN_INVALID);
        }
    }

    private UserAuth createGoogleUserAuth(String sub) {
        User newUser = User.createDefault();
        userRepository.save(newUser);

        UserAuth newAuth = UserAuth.createGoogle(newUser, sub);
        userAuthRepository.save(newAuth);

        return newAuth;
    }
}



