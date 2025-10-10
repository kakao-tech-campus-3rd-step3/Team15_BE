package katecam.hyuswim;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// basePackages 부분을 삭제해서 모든 컨트롤러에 적용되도록 변경
@ControllerAdvice
public class AdminControllerAdvice {

    @ModelAttribute("currentUri")
    public String addCurrentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
