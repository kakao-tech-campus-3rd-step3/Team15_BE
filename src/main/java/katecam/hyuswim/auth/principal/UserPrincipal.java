package katecam.hyuswim.auth.principal;

import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import katecam.hyuswim.user.User;
import katecam.hyuswim.user.UserRole;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final Long userId;
    private final UserRole role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }

  @Override
  public String getPassword() {
        return null;
  }

  @Override
  public String getUsername() {
      return String.valueOf(userId);
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
