package katecam.hyuswim.admin.controller;

import katecam.hyuswim.admin.dto.AdminCommentResponse;
import katecam.hyuswim.admin.service.AdminCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {

    private final AdminCommentService adminCommentService;
    private static final String ACTIVE_MENU = "comments";

    @GetMapping
    public String comments(Model model) {
        List<AdminCommentResponse> comments = adminCommentService.getComments();
        model.addAttribute("comments", comments);
        model.addAttribute("commentsActiveCount", comments.stream().filter(c -> !c.deleted()).count());
        model.addAttribute("commentsDeletedCount", comments.stream().filter(AdminCommentResponse::deleted).count());
        model.addAttribute("pageTitle", "댓글 관리");
        model.addAttribute("activeMenu", "comments");
        return "admin/comments/list"; // ✅ 여기서 layout 말고 list.html 리턴
    }


    @PostMapping("/{id}/hide")
    public String hideComment(@PathVariable Long id) {
        adminCommentService.hideComment(id);
        return "redirect:/admin/comments";
    }

    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable Long id) {
        adminCommentService.deleteComment(id);
        return "redirect:/admin/comments";
    }
}
