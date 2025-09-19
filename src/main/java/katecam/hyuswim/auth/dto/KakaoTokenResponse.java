package katecam.hyuswim.auth.dto;

public record KakaoTokenResponse(
        String access_token,
        String token_type,
        String refresh_token,
        int expires_in,
        int refresh_token_expires_in,
        String scope
) {}
