package com.defi.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class IpAddressInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler)
            throws Exception {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = "unknown";
        }
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty()) {
            userAgent = "unknown";
        }
        request.setAttribute("ipAddress", ipAddress);
        request.setAttribute("userAgent", userAgent);
        return true;
    }
}