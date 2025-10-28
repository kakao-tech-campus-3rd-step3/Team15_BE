package katecam.hyuswim.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import katecam.hyuswim.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // Authorization 헤더가 없거나 Bearer로 시작하지 않으면 비로그인 요청으로 간주
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7).trim();

        // 이미 인증된 상태라면 토큰 재검증 생략
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Authentication authentication = jwtUtil.getAuthentication(token);

                // 유효한 토큰일 경우에만 SecurityContext 설정
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                // 잘못된 토큰은 그냥 무시 (401/403 발생 X)
            } catch (Exception ignored) {
                // JWT 파싱 중 예외 발생해도 아무 조치 없이 요청 계속 진행
            }
        }

        filterChain.doFilter(request, response);
    }
}

