package katecam.hyuswim.auth.util;

import jakarta.servlet.http.HttpServletRequest;

public final class IpUtils {

    private IpUtils() {}

    public static String getClientIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}

