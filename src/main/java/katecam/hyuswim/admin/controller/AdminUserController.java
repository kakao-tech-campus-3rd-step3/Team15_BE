package katecam.hyuswim.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import katecam.hyuswim.admin.dto.BlockRequest;
import katecam.hyuswim.admin.dto.UserListResponse;
import katecam.hyuswim.admin.service.AdminUserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;
    private static final String ACTIVE_MENU = "users";

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<UserListResponse> users = adminUserService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("pageTitle", "사용자 관리");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        return "admin/users"; // ✅ users.html이 layout을 감쌈
    }

    @GetMapping("/{userId}")
    public String getUserDetail(@PathVariable Long userId, Model model) {
        var detail = adminUserService.getUserDetail(userId);

        model.addAttribute("userDetail", detail);
        model.addAttribute("pageTitle", "회원 상세 보기");
        model.addAttribute("activeMenu", "users");

        return "admin/user-detail"; 
    }



    @PostMapping("/{userId}/block")
    public String block(@PathVariable Long userId,
                        @ModelAttribute BlockRequest request,
                        RedirectAttributes ra) {
        var resp = adminUserService.blockUser(userId, request);
        ra.addFlashAttribute("msg", resp.getMessage());
        return "redirect:/admin/users";
    }

    @PostMapping("/{userId}/unblock")
    public String unblock(@PathVariable Long userId, RedirectAttributes ra) {
        var resp = adminUserService.unblockUser(userId);
        ra.addFlashAttribute("msg", resp.getMessage());
        return "redirect:/admin/users";
    }

    @PostMapping("/{userId}/ban")
    public String ban(@PathVariable Long userId, RedirectAttributes ra) {
        var resp = adminUserService.banUser(userId);
        ra.addFlashAttribute("msg", resp.getMessage());
        return "redirect:/admin/users";
    }
}
