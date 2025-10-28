package katecam.hyuswim.badge.event;

import katecam.hyuswim.badge.domain.BadgeKind;
import katecam.hyuswim.badge.service.BadgeService;
import katecam.hyuswim.comment.event.CommentCreatedEvent;
import katecam.hyuswim.like.event.PostLikedEvent;
import katecam.hyuswim.mission.event.MissionCompletedEvent;
import katecam.hyuswim.user.event.UserVisitedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class BadgeEventHandler {

    private final BadgeService badgeService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleComment(CommentCreatedEvent e) {
        badgeService.checkAndGrant(e.userId(), BadgeKind.DILIGENT_COMMENTER);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PostLikedEvent event) {
        badgeService.checkAndGrant(event.userId(), BadgeKind.LOVE_EVANGELIST);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMission(MissionCompletedEvent e) {
        badgeService.checkAndGrant(e.userId(), BadgeKind.MISSION_KILLER);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleVisit(UserVisitedEvent e) {
        badgeService.checkAndGrant(e.userId(), BadgeKind.PERFECT_ATTENDANCE);
    }
}
