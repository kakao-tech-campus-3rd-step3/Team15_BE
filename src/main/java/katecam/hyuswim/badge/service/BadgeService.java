package katecam.hyuswim.badge.service;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.badge.domain.*;
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

    // ----------------- 지급 -----------------

    /** 특정 종류만 점검/지급 */
    @Transactional
    public List<UserBadge> checkAndGrant(Long userId, BadgeKind kind) {
        int current = currentProgress(userId, kind);

        var user = userRepository.findById(userId).orElseThrow();
        var allBadges = badgeRepository.findByKindOrderByThresholdAsc(kind);

        // 보유 배지 id Set (O(1) 조회)
        Set<Long> owned = userBadgeRepository.findOwnedBadgeIdsByUserId(userId);

        List<UserBadge> newlyGranted = new ArrayList<>();
        for (Badge b : allBadges) {
            if (current >= b.getThreshold() && !owned.contains(b.getId())) {
                try {
                    var ub = new UserBadge(user, b);
                    newlyGranted.add(userBadgeRepository.save(ub));
                    owned.add(b.getId()); // 동일 루프 내 중복 방지
                } catch (DataIntegrityViolationException ignore) {
                    // 유니크 제약과 경합 시 안전하게 무시 (멱등성)
                }
            }
        }
        return newlyGranted;
    }

    /** 모든 종류 한 번에 점검/지급 (컬렉션 페이지 진입 시 보정용으로 유용) */
    @Transactional
    public void checkAndGrantAll(Long userId) {
        for (BadgeKind kind : BadgeKind.values()) {
            checkAndGrant(userId, kind);
        }
    }

    // ----------------- 조회(분류 뷰) -----------------

    @Transactional(readOnly = true)
    public BadgeCollectionVM getMyBadgeCollection(Long userId) {
        // 1) 마스터 전체, 유저 보유 이력
        List<Badge> allBadges = badgeRepository.findAll(); // 16개
        var userBadges = userBadgeRepository.findAllByUserId(userId);

        // 보유 배지 id -> earnedAt 맵
        Map<Long, java.time.LocalDateTime> earnedAtByBadgeId =
                userBadges.stream().collect(Collectors.toMap(
                        ub -> ub.getBadge().getId(),
                        UserBadge::getEarnedAt
                ));

        // 2) 진행도(집계) 한 번만 계산
        int likeCnt   = (int) postLikeRepository.countByUserId(userId);
        int cmtCnt    = (int) commentRepository.countActiveByUserId(userId);
        int missionDs = (int) missionProgressRepository.countDistinctDaysByUserId(userId);
        int visitDs   = (int) userVisitRepository.countDaysByUserId(userId);

        // 3) 배지별 ViewItem 생성
        List<BadgeViewItem> all = allBadges.stream()
                .map(b -> {
                    int cur = switch (b.getKind()) {
                        case LOVE_EVANGELIST    -> likeCnt;
                        case DILIGENT_COMMENTER -> cmtCnt;
                        case MISSION_KILLER     -> missionDs;
                        case PERFECT_ATTENDANCE -> visitDs;
                    };
                    boolean earned = earnedAtByBadgeId.containsKey(b.getId());
                    var earnedAt   = earned ? earnedAtByBadgeId.get(b.getId()) : null;
                    int percent = Math.max(0, Math.min(100,
                            (int) Math.round((cur * 100.0) / b.getThreshold())));

                    return BadgeViewItem.builder()
                            .badgeId(b.getId())
                            .kind(b.getKind())
                            .tier(b.getTier())
                            .name(b.getName())
                            .threshold(b.getThreshold())
                            .current(cur)
                            .earned(earned)
                            .earnedAt(earnedAt)
                            .percent(percent)
                            .build();
                })
                .sorted(Comparator
                        .comparing((BadgeViewItem v) -> v.getKind().name())
                        .thenComparing(BadgeViewItem::getThreshold))
                .toList();

        // 4) 분류
        var earned = all.stream().filter(BadgeViewItem::isEarned).toList();
        var inProgress = all.stream().filter(v -> !v.isEarned() && v.getCurrent() > 0).toList();
        var locked = all.stream().filter(v -> !v.isEarned() && v.getCurrent() == 0).toList();

        return BadgeCollectionVM.builder()
                .totalCount(all.size())
                .earnedCount(earned.size())
                .inProgressCount(inProgress.size())
                .lockedCount(locked.size())
                .earned(earned)
                .inProgress(inProgress)
                .locked(locked)
                .all(all)
                .build();
    }

    // ----------------- 내부 유틸 -----------------

    private int currentProgress(Long userId, BadgeKind kind) {
        return switch (kind) {
            case LOVE_EVANGELIST    -> (int) postLikeRepository.countByUserId(userId);
            case DILIGENT_COMMENTER -> (int) commentRepository.countActiveByUserId(userId);
            case MISSION_KILLER     -> (int) missionProgressRepository.countDistinctDaysByUserId(userId);
            case PERFECT_ATTENDANCE -> (int) userVisitRepository.countDaysByUserId(userId);
        };
    }
}
