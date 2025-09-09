package katecam.hyuswim.admin.controller;

import katecam.hyuswim.admin.dto.BlockRequest;
import katecam.hyuswim.admin.dto.BlockResponse;
import katecam.hyuswim.admin.dto.UserResponse;
import katecam.hyuswim.admin.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.getUserWithActivities(userId));
    }

    @PostMapping("/users/{userId}/block")
    public String block(@PathVariable Long userId,
                        @ModelAttribute BlockRequest request,
                        RedirectAttributes ra) {
        adminUserService.blockUser(userId, request);
        ra.addAttribute("msg", "사용자 차단 완료: " + userId);
        return "redirect:/admin";
    }

    @PostMapping("/{userId}:unblock")
    public ResponseEntity<BlockResponse> unblockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.unblockUser(userId));
    }

    @PostMapping("/{userId}:ban")
    public ResponseEntity<BlockResponse> banUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.banUser(userId));
    }
}
