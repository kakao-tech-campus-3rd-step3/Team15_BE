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

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<UserListResponse> users = adminUserService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("content", "admin/users :: content");
        return "admin/layout";
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

