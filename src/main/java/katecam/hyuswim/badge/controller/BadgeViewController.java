package katecam.hyuswim.badge.controller;

import katecam.hyuswim.badge.repository.UserBadgeRepository;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BadgeViewController {

    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;

    @GetMapping("/badges/test")
    public String badgeTest(Model model) {
        Long userId = 1L; // 테스트용 사용자 ID (실제 로그인 사용자로 대체 가능)
        var user = userRepository.findById(userId).orElseThrow();

        var userBadges = userBadgeRepository.findAllByUserId(userId);

        model.addAttribute("user", user);
        model.addAttribute("badges", userBadges);
        model.addAttribute("totalBadgeCount", 16); // 총 배지 개수
        model.addAttribute("earnedCount", userBadges.size());

        return "badges/test"; // templates/badges/test.html
    }
}
