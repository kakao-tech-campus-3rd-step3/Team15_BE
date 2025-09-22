package katecam.hyuswim.auth.domain;

import jakarta.persistence.*;
import katecam.hyuswim.auth.dto.SignupRequest;
import katecam.hyuswim.user.domain.AuthProvider;
import katecam.hyuswim.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
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

    public static UserAuth createLocal(User user, SignupRequest request, String encodedPassword) {
        return new UserAuth(user, AuthProvider.LOCAL, null, request.getEmail(), encodedPassword);
    }

    public static UserAuth createKakao(User user, String kakaoId) {
        return new UserAuth(user, AuthProvider.KAKAO, kakaoId, null, null);
    }
}
