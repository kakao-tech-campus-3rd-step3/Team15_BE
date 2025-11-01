package katecam.hyuswim.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HtmlExtensionRedirectConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HtmlRedirectInterceptor());
    }

    static class HtmlRedirectInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String uri = request.getRequestURI();
            if (uri.endsWith(".html")) {
                String redirectTo = uri.substring(0, uri.length() - 5); // ".html" 제거
                response.sendRedirect(redirectTo);
                return false;
            }
            return true;
        }
    }
}
