package com.cinema.infrastructure.config;

import com.cinema.presentation.interceptor.IdempotencyInterceptor;
import com.cinema.presentation.interceptor.RequestCorrelationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final RequestCorrelationInterceptor requestCorrelationInterceptor;

    private final IdempotencyInterceptor idempotencyInterceptor;
    public WebConfig(RequestCorrelationInterceptor requestCorrelationInterceptor, IdempotencyInterceptor idempotencyInterceptor) {
        this.requestCorrelationInterceptor = requestCorrelationInterceptor;
        this.idempotencyInterceptor = idempotencyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestCorrelationInterceptor);
        registry.addInterceptor(idempotencyInterceptor).addPathPatterns("/api/payments", "/api/payments/");
    }
}
