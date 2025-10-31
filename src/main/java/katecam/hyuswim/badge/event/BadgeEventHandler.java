package katecam.hyuswim.badge.event;

import katecam.hyuswim.badge.domain.BadgeKind;
import katecam.hyuswim.badge.service.BadgeService;
import katecam.hyuswim.comment.event.CommentCreatedEvent;
import katecam.hyuswim.like.event.PostLikedEvent;
import katecam.hyuswim.mission.event.MissionCompletedEvent;
import katecam.hyuswim.user.event.UserVisitedEvent;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class BadgeEventHandler {

    private final BadgeService badgeService;
    private final UserRepository userRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleComment(CommentCreatedEvent event) {
        userRepository.findById(event.userId()).ifPresent(user ->
                badgeService.checkAndGrant(user, BadgeKind.DILIGENT_COMMENTER)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostLike(PostLikedEvent event) {
        userRepository.findById(event.userId()).ifPresent(user ->
                badgeService.checkAndGrant(user, BadgeKind.LOVE_EVANGELIST)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMission(MissionCompletedEvent event) {
        userRepository.findById(event.userId()).ifPresent(user ->
                badgeService.checkAndGrant(user, BadgeKind.MISSION_KILLER)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleVisit(UserVisitedEvent event) {
        userRepository.findById(event.userId()).ifPresent(user ->
                badgeService.checkAndGrant(user, BadgeKind.PERFECT_ATTENDANCE)
        );
    }
}

