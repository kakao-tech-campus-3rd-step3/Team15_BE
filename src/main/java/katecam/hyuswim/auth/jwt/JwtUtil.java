package katecam.hyuswim.auth.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import katecam.hyuswim.user.UserRole;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  private JwtParser jwtParser;

  private final long TOKEN_EXPIRATION_TIME_MS = 60 * 60 * 1000;

  @PostConstruct
  public void init() {
    jwtParser = Jwts.parser().setSigningKey(secret).build();
  }

  public Long extractUserId(String token) {
    return Long.valueOf(jwtParser.parseClaimsJws(token).getBody().getSubject());
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

  public Boolean validateToken(String token) {
    try {
      jwtParser.parseClaimsJws(token);
      return !isTokenExpired(token);
    } catch (JwtException e) {
      return false;
    }
  }

  private Boolean isTokenExpired(String token) {
    return jwtParser.parseClaimsJws(token).getBody().getExpiration().before(new Date());
  }
}
