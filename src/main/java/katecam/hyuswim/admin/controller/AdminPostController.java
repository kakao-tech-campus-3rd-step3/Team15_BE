package katecam.hyuswim.admin.controller;

import katecam.hyuswim.admin.dto.AdminPostDetailResponse;
import katecam.hyuswim.admin.dto.AdminPostListResponse;
import katecam.hyuswim.admin.service.AdminPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/posts")
public class AdminPostController {

    private final AdminPostService adminPostService;
    private static final String ACTIVE_MENU = "posts";

    @GetMapping
    public String posts(Model model) {
        List<AdminPostListResponse> posts = adminPostService.getPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("pageTitle", "게시글 관리");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        // ✅ 이제 layout 말고 list.html 직접 반환
        return "admin/posts/list";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable Long postId, Model model) {
        AdminPostDetailResponse post = adminPostService.getPost(postId);
        model.addAttribute("post", post);
        model.addAttribute("pageTitle", "게시글 상세");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        // ✅ layout 말고 detail.html 직접 반환
        return "admin/posts/detail";
    }

    @PostMapping("/{postId}/hide")
    public String hidePost(@PathVariable Long postId) {
        adminPostService.hidePost(postId);
        return "redirect:/admin/posts";
    }

    @PostMapping("/{postId}/unhide")
    public String unhidePost(@PathVariable Long postId) {
        adminPostService.unhidePost(postId);
        return "redirect:/admin/posts";
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId) {
        adminPostService.deletePost(postId);
        return "redirect:/admin/posts";
    }
}
