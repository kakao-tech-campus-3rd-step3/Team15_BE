package katecam.hyuswim.user.service;

import java.time.LocalDate;

import katecam.hyuswim.badge.domain.BadgeKind;
import katecam.hyuswim.badge.service.BadgeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.domain.UserVisit;
import katecam.hyuswim.user.repository.UserRepository;
import katecam.hyuswim.user.repository.UserVisitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserVisitService {
    private final UserRepository userRepository;
    private final UserVisitRepository userVisitRepository;
    private final BadgeService badgeService;

    @Transactional
    public void touch(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        var today = LocalDate.now();
        if (!userVisitRepository.existsByUser_IdAndVisitDate(userId, today)) {
            userVisitRepository.saveAndFlush(new UserVisit(user, today));
            badgeService.checkAndGrant(userId, BadgeKind.PERFECT_ATTENDANCE);
        }
    }
}

