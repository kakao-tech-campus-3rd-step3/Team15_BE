package katecam.hyuswim.admin.controller;

import katecam.hyuswim.admin.service.AdminUserService; // ← 서비스 경로 맞춰서 import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminUserService adminUserService;

    public AdminController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("users", adminUserService.findAll()); // ← 여기만 추가
        return "admin/admin_dashboard";
    }
}
