package katecam.hyuswim.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleIdTokenPayload(
        String iss,
        String sub,
        String aud,
        long exp,
        long iat
) {}

