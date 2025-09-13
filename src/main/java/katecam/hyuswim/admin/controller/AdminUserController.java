package katecam.hyuswim.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import katecam.hyuswim.admin.dto.BlockRequest;
import katecam.hyuswim.admin.dto.BlockResponse;
import katecam.hyuswim.admin.dto.UserDetailResponse;
import katecam.hyuswim.admin.dto.UserListResponse;
import katecam.hyuswim.admin.service.AdminUserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public String dashboard(@RequestParam(required = false) Long userId, Model model) {
        List<UserListResponse> users = adminUserService.findAll();
        model.addAttribute("users", users);

        if (userId != null) {
            UserDetailResponse detail = adminUserService.getUserWithActivities(userId);
            model.addAttribute("detail", detail);
        }

        return "admin/admin_dashboard"; // 단일 템플릿
    }

    @PostMapping("/users/{userId}/block")
    public String block(@PathVariable Long userId,
                        @ModelAttribute BlockRequest request,
                        RedirectAttributes ra) {
        BlockResponse resp = adminUserService.blockUser(userId, request);
        ra.addFlashAttribute("msg", resp.getMessage());
        return "redirect:/admin?userId=" + userId;
    }

    @PostMapping("/users/{userId}/unblock")
    public String unblock(@PathVariable Long userId, RedirectAttributes ra) {
        BlockResponse resp = adminUserService.unblockUser(userId);
        ra.addFlashAttribute("msg", resp.getMessage());
        return "redirect:/admin?userId=" + userId;
    }

    @PostMapping("/users/{userId}/ban")
    public String ban(@PathVariable Long userId, RedirectAttributes ra) {
        BlockResponse resp = adminUserService.banUser(userId);
        ra.addFlashAttribute("msg", resp.getMessage());
        return "redirect:/admin?userId=" + userId;
    }
}
