package katecam.hyuswim.admin.controller;

import jakarta.validation.Valid;
import katecam.hyuswim.admin.dto.SupportForm;
import katecam.hyuswim.admin.service.AdminSupportService;
import katecam.hyuswim.support.domain.Support;
import katecam.hyuswim.support.domain.SupportType;
import katecam.hyuswim.support.dto.SupportDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/supports")
@RequiredArgsConstructor
public class AdminSupportViewController {

    private static final String ACTIVE_MENU = "supports";
    private final AdminSupportService adminSupportService;

    /** 지원 사업 목록 페이지 */
    @GetMapping
    public String supportList(Model model) {
        List<Support> supports = adminSupportService.getAllSupports(); // ✅ DB 전체 조회
        model.addAttribute("pageTitle", "지원 사업 관리");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        model.addAttribute("supports", supports); // ✅ 모델에 담기
        return "admin/support/list";
    }

    /** 등록 폼 페이지 */
    @GetMapping("/form")
    public String supportForm(Model model) {
        model.addAttribute("pageTitle", "지원 사업 등록");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        model.addAttribute("form", new SupportForm(null, null, null, null, null, null));
        return "admin/support/form";
    }

    /** 등록 처리 */
    @PostMapping
    public String createSupport(@Valid @ModelAttribute("form") SupportForm form,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "지원 사업 등록");
            model.addAttribute("activeMenu", ACTIVE_MENU);
            return "admin/support/form";
        }

        Support support = Support.builder()
                .name(form.name())
                .company(form.company())
                .content(form.content())
                .place(form.place())
                .endDate(form.endDate())
                .supportType(parseSupportType(form.supportType())) // ✅ 문자열 → Enum 변환
                .build();

        adminSupportService.createSupport(support);
        return "redirect:/admin/supports"; // ✅ 저장 후 목록으로 리다이렉트
    }

    /** 상세 페이지 */
    @GetMapping("/detail")
    public String supportDetail(@RequestParam(name = "id", required = false) Long id, Model model) {
        model.addAttribute("pageTitle", "지원 사업 상세");
        model.addAttribute("activeMenu", ACTIVE_MENU);
        model.addAttribute("id", id);
        return "admin/support/detail";
    }

    /** 문자열로 넘어온 지원사업 유형을 Enum으로 변환 */
    private SupportType parseSupportType(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }
        try {
            return SupportType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
