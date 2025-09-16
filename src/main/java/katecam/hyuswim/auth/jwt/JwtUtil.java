package katecam.hyuswim.auth.jwt;

import java.util.Date;

import io.jsonwebtoken.*;
import katecam.hyuswim.auth.principal.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import katecam.hyuswim.user.domain.UserRole;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  private JwtParser jwtParser;

  private static final long TOKEN_EXPIRATION_TIME_MS = 60 * 60 * 1000; //1시간

  @PostConstruct
  public void init() {
    jwtParser = Jwts.parser().setSigningKey(secret).build();
  }

  public String generateToken(Long userId, UserRole role) {
      long now = System.currentTimeMillis();
      return Jwts.builder()
              .setSubject(String.valueOf(userId))
              .claim("role", role.name())
              .setIssuedAt(new Date(now))
              .setExpiration(new Date(now + TOKEN_EXPIRATION_TIME_MS))
              .signWith(SignatureAlgorithm.HS256, secret)
              .compact();
  }

    public Claims getClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token).getBody();
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
        return new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities()
        );
    }

}
