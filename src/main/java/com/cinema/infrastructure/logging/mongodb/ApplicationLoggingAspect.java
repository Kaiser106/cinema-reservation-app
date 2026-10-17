package com.cinema.infrastructure.logging.mongodb;

import com.cinema.infrastructure.auth.SessionPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Aspect
@Component
public class ApplicationLoggingAspect {
    private final LogRepository logRepository;
    private final HttpServletRequest request;

    public ApplicationLoggingAspect(LogRepository logRepository, HttpServletRequest request) {
        this.logRepository = logRepository;
        this.request = request;
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logHttp(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        LogDocument doc = new LogDocument();
        doc.setTimestamp(Instant.now());
        doc.setCorrelationId(Optional.ofNullable((String) request.getAttribute("correlationId")).orElse("N/A"));
        doc.setHttpMethod(request.getMethod());
        doc.setEndpoint(request.getRequestURI());
        doc.setOperation(pjp.getSignature().toShortString());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SessionPrincipal principal) {
            doc.setUserId(principal.userId().toString());
        }

        try {
            Object result = pjp.proceed();
            doc.setSuccess(true);
            return result;
        } catch (Throwable ex) {
            doc.setSuccess(false);
            doc.setException(ex.getClass().getSimpleName() + ":" + ex.getMessage());
            throw ex;
        } finally {
            doc.setDurationMs(System.currentTimeMillis() - start);
            // Sensitive secrets are never logged; only technical metadata is stored.
            logRepository.save(doc);
        }
    }
}
