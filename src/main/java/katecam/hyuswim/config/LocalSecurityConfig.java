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

  @Bean
  public UserDetailsService localUsers(PasswordEncoder encoder) {
    var admin =
        User.withUsername("admin").password(encoder.encode("admin123!")).roles("ADMIN").build();
    return new InMemoryUserDetailsManager(admin);
  }

  @Bean
  @Order(1)
  public SecurityFilterChain h2ConsoleChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/h2-console/**")
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions(fo -> fo.sameOrigin()));
    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain appChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**")
                    .permitAll()
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .formLogin(Customizer.withDefaults())
        .logout(lo -> lo.logoutSuccessUrl("/login?logout"))
        .csrf(Customizer.withDefaults())
        .httpBasic(basic -> basic.disable());
    return http.build();
  }
}
