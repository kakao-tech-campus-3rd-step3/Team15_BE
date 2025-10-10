package katecam.hyuswim.auth.domain;

import jakarta.persistence.*;
import katecam.hyuswim.auth.dto.SignupRequest;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class UserAuth {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider provider;

    private String providerId;
    private String email;
    private String password;

    @Column(name = "password_last_changed")
    @CreatedDate
    private LocalDateTime passwordLastChanged;

    private UserAuth(User user, AuthProvider provider, String providerId, String email, String password) {
        this.user = user;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.password = password;
    }
    public UserAuth(User user, AuthProvider provider) {
        this.user = user;
        this.provider = provider;
    }

    public static UserAuth createLocal(User user, SignupRequest request, String encodedPassword) {
        return new UserAuth(user, AuthProvider.LOCAL, null, request.getEmail(), encodedPassword);
    }

    public static UserAuth createKakao(User user, String kakaoId) {
        return new UserAuth(user, AuthProvider.KAKAO, kakaoId, null, null);
    }

    public static UserAuth createGoogle(User user, String googleId) {
        return new UserAuth(user, AuthProvider.GOOGLE, googleId, null, null);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
}
