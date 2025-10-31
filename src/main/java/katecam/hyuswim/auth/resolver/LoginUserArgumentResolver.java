package katecam.hyuswim.auth.resolver;

import katecam.hyuswim.auth.annotation.LoginUser;
import katecam.hyuswim.auth.principal.UserPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginUser.class);
  }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 비로그인 상태면 null 반환 (비로그인 GET 허용)
        if (authentication == null ||
                authentication.getPrincipal() == null ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        // 인증된 사용자만 UserPrincipal 캐스팅
        if (!(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // DB에서 실제 User 엔티티 조회
        return userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
