package katecam.hyuswim.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;
import katecam.hyuswim.auth.jwt.JwtFilter;

@Configuration
@Profile("local")
public class LocalSecurityConfig {

  @Bean
  @ConditionalOnMissingBean(PasswordEncoder.class)
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @ConditionalOnMissingBean(UserDetailsService.class)
  public UserDetailsService localUsers(PasswordEncoder encoder) {
    var admin =
        User.withUsername("admin").password(encoder.encode("admin123!")).roles("ADMIN").build();
    return new InMemoryUserDetailsManager(admin);
  }

  // API 전용
  @Bean
  @Order(1)
  public SecurityFilterChain apiChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
      http.securityMatcher("/api/**")
              .authorizeHttpRequests(auth -> auth
                      .requestMatchers("/api/user/signup", "/api/auth/login").permitAll()
                      .requestMatchers("/api/admin/**").hasRole("ADMIN")
                      .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                      .anyRequest().authenticated()
              )
              .csrf(csrf -> csrf.disable())
              .formLogin(form -> form.disable())
              .httpBasic(Customizer.withDefaults())
              .exceptionHandling(ex -> ex
                      .authenticationEntryPoint((req, res, e) -> {
                          res.setContentType("application/json");
                          res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                          res.getWriter().write("{\"error\":\"Unauthorized\"}");
                      })
                      .accessDeniedHandler((req, res, e) -> {
                          res.setContentType("application/json");
                          res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                          res.getWriter().write("{\"error\":\"Forbidden\"}");
                      })
              )
              .addFilterBefore(
                      jwtFilter,
                      org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
              );

      return http.build();
  }


    // Admin 전용
  @Bean
  @Order(2)
  public SecurityFilterChain adminChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/admin/**")
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**")
                    .permitAll()
                    .anyRequest()
                    .hasRole("ADMIN"))
        .formLogin(Customizer.withDefaults()) // 여기서는 HTML 로그인 페이지
        .logout(lo -> lo.logoutSuccessUrl("/login?logout"))
        .csrf(Customizer.withDefaults());
    return http.build();
  }
}
