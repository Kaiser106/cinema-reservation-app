package com.cinema.presentation.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/** Requires an Idempotency-Key header on payment POSTs and exposes it as a request attribute for the controller. */
@Component
public class IdempotencyInterceptor implements HandlerInterceptor {
    public static final String HEADER = "Idempotency-Key";
    public static final String ATTRIBUTE = "idempotencyKey";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!"POST".equalsIgnoreCase(request.getMethod())) return true;
        String key = request.getHeader(HEADER);
        if (key == null || key.isBlank() || key.length() > 120) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Idempotency-Key header is required (max 120 chars)");
            return false;
        }
        request.setAttribute(ATTRIBUTE, key.trim());
        return true;
    }
}
