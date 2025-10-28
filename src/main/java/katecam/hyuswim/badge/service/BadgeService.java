package katecam.hyuswim.badge.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import katecam.hyuswim.badge.domain.*;
import katecam.hyuswim.badge.dto.EarnedBadgeResponse;
import katecam.hyuswim.badge.repository.BadgeRepository;
import katecam.hyuswim.badge.repository.UserBadgeRepository;
import katecam.hyuswim.badge.view.BadgeCollectionVM;
import katecam.hyuswim.badge.view.BadgeViewItem;
import katecam.hyuswim.comment.repository.CommentRepository;
import katecam.hyuswim.like.repository.PostLikeRepository;
import katecam.hyuswim.mission.repository.MissionProgressRepository;
import katecam.hyuswim.user.repository.UserRepository;
import katecam.hyuswim.user.repository.UserVisitRepository;

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
        int progress = getCurrentProgress(userId, kind);

        var user = userRepository.findById(userId).orElseThrow();
        var badgeList = badgeRepository.findByKindOrderByThresholdAsc(kind);
        var ownedIds = userBadgeRepository.findOwnedBadgeIds(userId);

        List<UserBadge> granted = new ArrayList<>();

        for (Badge badge : badgeList) {
            if (progress >= badge.getThreshold() && !ownedIds.contains(badge.getId())) {
                try {
                    granted.add(userBadgeRepository.save(new UserBadge(user, badge)));
                    ownedIds.add(badge.getId());
                } catch (DataIntegrityViolationException ignore) {
                    // Unique constraint 경쟁 시 무시 (멱등성 보장)
                }
            }
        }
        return granted;
    }

    @Transactional
    public void checkAndGrantAll(Long userId) {
        Arrays.stream(BadgeKind.values()).forEach(kind -> checkAndGrant(userId, kind));
    }

    @Transactional(readOnly = true)
    public List<EarnedBadgeResponse> getEarnedBadges(Long userId) {
        return userBadgeRepository.findAllByUser_Id(userId).stream()
                .map(EarnedBadgeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BadgeCollectionVM getMyBadgeCollection(Long userId) {
        List<Badge> allBadges = badgeRepository.findAll();
        var userBadges = userBadgeRepository.findAllByUser_Id(userId);

        Map<Long, java.time.LocalDateTime> earnedAtMap = userBadges.stream()
                .collect(Collectors.toMap(
                        ub -> ub.getBadge().getId(),
                        UserBadge::getEarnedAt
                ));

        var progressMap = getProgressMap(userId);

        List<BadgeViewItem> viewItems = allBadges.stream()
                .map(b -> {
                    int current = progressMap.get(b.getKind());
                    boolean earned = earnedAtMap.containsKey(b.getId());
                    LocalDateTime earnedAt = earned ? earnedAtMap.get(b.getId()) : null;
                    int percent = Math.min(100, (int) Math.round(current * 100.0 / b.getThreshold()));

                    return BadgeViewItem.builder()
                            .badgeId(b.getId())
                            .kind(b.getKind())
                            .tier(b.getTier())
                            .name(b.getName())
                            .threshold(b.getThreshold())
                            .current(current)
                            .earned(earned)
                            .earnedAt(earnedAt)
                            .percent(percent)
                            .build();
                })
                .sorted(Comparator.comparing((BadgeViewItem v) -> v.getKind().name())
                        .thenComparing(BadgeViewItem::getThreshold))
                .toList();

        return BadgeCollectionVM.of(viewItems);
    }

    private Map<BadgeKind, Integer> getProgressMap(Long userId) {
        return Map.of(
                BadgeKind.LOVE_EVANGELIST, (int) postLikeRepository.countByUserId(userId),
                BadgeKind.DILIGENT_COMMENTER, (int) commentRepository.countActiveByUserId(userId),
                BadgeKind.MISSION_KILLER, (int) missionProgressRepository.countDistinctDaysByUserId(userId),
                BadgeKind.PERFECT_ATTENDANCE, (int) userVisitRepository.countDaysByUserId(userId)
        );
    }

    private int getCurrentProgress(Long userId, BadgeKind kind) {
        return switch (kind) {
            case LOVE_EVANGELIST -> (int) postLikeRepository.countByUserId(userId);
            case DILIGENT_COMMENTER -> (int) commentRepository.countActiveByUserId(userId);
            case MISSION_KILLER -> (int) missionProgressRepository.countDistinctDaysByUserId(userId);
            case PERFECT_ATTENDANCE -> (int) userVisitRepository.countDaysByUserId(userId);
        };
    }
}
