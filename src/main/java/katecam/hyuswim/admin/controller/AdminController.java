package katecam.hyuswim.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import katecam.hyuswim.admin.service.AdminUserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final AdminUserService adminUserService;

  public AdminController(AdminUserService adminUserService) {
    this.adminUserService = adminUserService;
  }

  @GetMapping
  public String dashboard(Model model) {
    model.addAttribute("users", adminUserService.findAll());
    return "admin/admin_dashboard";
  }

  @PostMapping("/users/{userId}/block")
  public String block(@PathVariable Long userId, RedirectAttributes ra) {
    adminUserService.blockUser(userId);
    ra.addAttribute("msg", "사용자 차단 완료: " + userId);
    return "redirect:/admin";
  }

  @PostMapping("/users/{userId}/unblock")
  public String unblock(@PathVariable Long userId, RedirectAttributes ra) {
    adminUserService.unblockUser(userId);
    ra.addAttribute("msg", "사용자 차단 해제: " + userId);
    return "redirect:/admin";
  }
}
