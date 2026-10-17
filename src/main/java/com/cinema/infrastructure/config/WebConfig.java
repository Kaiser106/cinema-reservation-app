package com.cinema.infrastructure.config;

import com.cinema.presentation.interceptor.RequestCorrelationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final RequestCorrelationInterceptor requestCorrelationInterceptor;

    public WebConfig(RequestCorrelationInterceptor requestCorrelationInterceptor) {
        this.requestCorrelationInterceptor = requestCorrelationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestCorrelationInterceptor);
    }
}
