//package katecam.hyuswim.badge.controller;
//
//import katecam.hyuswim.badge.domain.BadgeKind;
//import katecam.hyuswim.badge.service.BadgeService;
//import katecam.hyuswim.comment.dto.CommentRequest;
//import katecam.hyuswim.comment.service.CommentService;
//import katecam.hyuswim.like.service.PostLikeService;
//import katecam.hyuswim.mission.service.MissionService;
//import katecam.hyuswim.user.domain.User;
//import katecam.hyuswim.user.repository.UserRepository;
//import katecam.hyuswim.user.service.UserVisitService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/test")
//public class BadgeTestController {
//
//    private final PostLikeService postLikeService;
//    private final CommentService commentService;
//    private final MissionService missionService;
//    private final UserVisitService userVisitService;
//    private final UserRepository userRepository;
//
//    @PostMapping("/like")
//    public String addLike() {
//        User user = userRepository.findById(1L).orElseThrow(); // 테스트용 유저
//        postLikeService.addLike(1L, user); // postId=1에 좋아요
//        return "redirect:/badges/test";
//    }
//
//    @PostMapping("/comment")
//    public String addComment(@RequestParam String content) {
//        User user = userRepository.findById(1L).orElseThrow();
//        commentService.createComment(user, 1L, new CommentRequest(content, false)); // postId=1
//        return "redirect:/badges/test";
//    }
//
//    @PostMapping("/mission")
//    public String startMission() {
//        missionService.startMission(1L, 1L); // userId=1, missionId=1
//        return "redirect:/badges/test";
//    }
//
//    @PostMapping("/visit")
//    public String visit() {
//        userVisitService.touch(1L); // userId=1
//        return "redirect:/badges/test";
//    }
//}

