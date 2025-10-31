//package katecam.hyuswim.badge.controller;
//
//import katecam.hyuswim.badge.service.BadgeService;
//import katecam.hyuswim.badge.view.BadgeCollectionVM;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//@RequiredArgsConstructor
//public class BadgeViewController {
//
//    private final BadgeService badgeService;
//
//    @GetMapping("/badges/me")
//    public String myBadges(Model model) {
//        Long userId = 1L;
//        badgeService.checkAndGrantAll(userId);
//        var vm = badgeService.getMyBadgeCollection(userId);
//        model.addAttribute("vm", vm);
//        return "badges/collection";
//    }
//}
