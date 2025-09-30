package katecam.hyuswim.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/supports")
public class AdminSupportViewController {

    private static final String ACTIVE_MENU = "supports";

    @GetMapping
    public String supportList(Model model) {
        model.addAttribute("pageTitle", "지원 사업 관리");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        return "admin/support/list";  // templates/admin/support/list.html
    }

    @GetMapping("/form")
    public String supportForm(Model model) {
        model.addAttribute("pageTitle", "지원 사업 등록");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        return "admin/support/form";  // templates/admin/support/form.html
    }

    @GetMapping("/detail")
    public String supportDetail(Model model) {
        model.addAttribute("pageTitle", "지원 사업 상세");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        return "admin/support/detail";  // templates/admin/support/detail.html
    }
}
