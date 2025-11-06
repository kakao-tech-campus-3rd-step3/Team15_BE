package katecam.hyuswim.auth.util;

import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import katecam.hyuswim.auth.principal.UserPrincipal;
import katecam.hyuswim.user.domain.UserRole;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private JwtParser jwtParser;
    private SecretKey key;

    private static final long ACCESS_TOKEN_EXPIRATION_TIME_MS = 60 * 60 * 1000; // 1시간
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000; // 7일

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();
    }

    public String generateAccessToken(Long userId, UserRole role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role.name())
                .issuedAt(new Date(now))
                .expiration(new Date(now + ACCESS_TOKEN_EXPIRATION_TIME_MS))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(Long userId, UserRole role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role.name())
                .issuedAt(new Date(now))
                .expiration(new Date(now + REFRESH_TOKEN_EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) {
        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            return null;
        }
    }

    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        if (claims == null || isTokenExpired(claims)) return null;

        Long userId = Long.valueOf(claims.getSubject());
        UserRole role = UserRole.valueOf(claims.get("role", String.class));

        UserPrincipal principal = new UserPrincipal(userId, role);
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }
}
