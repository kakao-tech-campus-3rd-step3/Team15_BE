package katecam.hyuswim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("local")
public class LocalSecurityConfig {


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // ↓↓↓ 추가: 인메모리 ADMIN 계정 (로컬 전용)
  @Bean
  public UserDetailsService localUsers(PasswordEncoder encoder) {
    var admin = User.withUsername("admin")
            .password(encoder.encode("admin123!"))
            .roles("ADMIN") // → ROLE_ADMIN 권한 부여
            .build();
    return new InMemoryUserDetailsManager(admin);
  }

  // 1) H2 콘솔만 담당하는 보안 체인 (우선순위 높음)
  @Bean
  @Order(1)
  public SecurityFilterChain h2ConsoleChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/h2-console/**")
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .csrf(csrf -> csrf.disable()) // H2 폼/프레임 이슈 회피
        .headers(headers -> headers.frameOptions(fo -> fo.sameOrigin()));
    return http.build();
  }

  // 2) 애플리케이션 기본 체인 (그 다음 평가)
  @Bean
  @Order(2)
  public SecurityFilterChain appChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**")
                    .permitAll()
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN") // ★ ADMIN만 접근
                    .anyRequest()
                    .authenticated())
        .formLogin(Customizer.withDefaults())
        .logout(lo -> lo.logoutSuccessUrl("/login?logout"))
        .csrf(Customizer.withDefaults())
        .httpBasic(basic -> basic.disable());
    return http.build();
  }
}
