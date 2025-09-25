package katecam.hyuswim.counseling.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class CounselingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "step", length = 50)
    private CounselingStep step;

    private LocalDateTime startedAt;
    private LocalDateTime lastMessageAt;
    private LocalDateTime endedAt;

    @Lob
    private String messages;

    public CounselingSession(CounselingStep step) {
        this.step = step;
        this.startedAt = LocalDateTime.now();
        this.messages = "[]";
    }

    public void updateStep(CounselingStep step) {
        this.step = step;
    }

    public void updateMessages(String messagesJson) {
        this.messages = messagesJson;
        this.lastMessageAt = LocalDateTime.now();
    }

    public void end() {
        this.endedAt = LocalDateTime.now();
        this.step = CounselingStep.CLOSED;
    }

    public void closeIfInactive() {
        this.step = CounselingStep.CLOSED;
    }
}
