package katecam.hyuswim.badge.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import katecam.hyuswim.badge.domain.*;
import katecam.hyuswim.badge.repository.BadgeRepository;
import katecam.hyuswim.badge.repository.UserBadgeRepository;
import katecam.hyuswim.comment.repository.CommentRepository;
import katecam.hyuswim.like.repository.PostLikeRepository;
import katecam.hyuswim.mission.repository.MissionProgressRepository;
import katecam.hyuswim.user.repository.UserRepository;
import katecam.hyuswim.user.repository.UserVisitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;

    // 집계 소스
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final MissionProgressRepository missionProgressRepository;
    private final UserVisitRepository userVisitRepository;

    @Transactional
    public List<UserBadge> checkAndGrant(Long userId, BadgeKind kind) {
        int current = switch (kind) {
            case LOVE_EVANGELIST   -> (int) postLikeRepository.countByUserId(userId);
            case DILIGENT_COMMENTER-> (int) commentRepository.countActiveByUserId(userId);
            case MISSION_KILLER    -> (int) missionProgressRepository.countDistinctDaysByUserId(userId);
            case PERFECT_ATTENDANCE-> (int) userVisitRepository.countDaysByUserId(userId);
        };

        var user = userRepository.findById(userId).orElseThrow();
        var allBadges = badgeRepository.findByKindOrderByThresholdAsc(kind);
        var owned    = userBadgeRepository.findOwnedBadgeIdsByUserId(userId);

        List<UserBadge> newlyGranted = new ArrayList<>();
        for (Badge b : allBadges) {
            if (current >= b.getThreshold() && !owned.contains(b.getId())) {
                var ub = new UserBadge(user, b);
                newlyGranted.add(userBadgeRepository.save(ub));
            }
        }
        return newlyGranted;
    }
}

