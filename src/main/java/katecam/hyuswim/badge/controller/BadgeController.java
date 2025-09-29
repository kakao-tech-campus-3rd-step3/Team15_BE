package katecam.hyuswim.badge.controller;

import katecam.hyuswim.auth.annotation.LoginUser;
import katecam.hyuswim.badge.domain.Badge;
import katecam.hyuswim.badge.domain.BadgeKind;
import katecam.hyuswim.badge.domain.UserBadge;
import katecam.hyuswim.badge.repository.BadgeRepository;
import katecam.hyuswim.badge.repository.UserBadgeRepository;
import katecam.hyuswim.badge.service.BadgeService;
import katecam.hyuswim.badge.view.BadgeCollectionVM;
import katecam.hyuswim.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/badges")
public class BadgeController {

    private final BadgeService badgeService;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    // 컬렉션(획득/진행중/잠김/전체)
    @GetMapping("/collection")
    public ResponseEntity<BadgeCollectionVM> getCollection(@LoginUser User loginUser) {
        // 보정: 임계치 달성했는데 과거에 미지급된 배지 지급
        badgeService.checkAndGrantAll(loginUser.getId());

        var vm = badgeService.getMyBadgeCollection(loginUser.getId());
        return ResponseEntity.ok(vm);
    }

    // 전체 배지 목록
    @GetMapping("/master")
    public ResponseEntity<List<Badge>> getMaster() {
        return ResponseEntity.ok(badgeRepository.findAll());
    }

    // 내가 획득한 배지 목록
    @GetMapping("/me")
    public ResponseEntity<List<EarnedBadgeResponse>> getMyBadges(@LoginUser User loginUser) {
        List<UserBadge> list = userBadgeRepository.findAllByUserId(loginUser.getId());
        return ResponseEntity.ok(
                list.stream().map(ub -> new EarnedBadgeResponse(
                        ub.getBadge().getId(),
                        ub.getBadge().getName(),
                        ub.getBadge().getKind(),
                        ub.getBadge().getTier().name(),
                        ub.getBadge().getThreshold(),
                        ub.getEarnedAt()
                )).toList()
        );
    }

    public record EarnedBadgeResponse(
            Long badgeId,
            String name,
            BadgeKind kind,
            String tier,
            Integer threshold,
            LocalDateTime earnedAt
    ) {}
}
