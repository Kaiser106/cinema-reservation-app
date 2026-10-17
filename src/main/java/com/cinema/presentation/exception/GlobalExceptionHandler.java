package com.cinema.presentation.exception;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.LocalizedMessageService;
import com.cinema.presentation.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final LocalizedMessageService messageService;

    public GlobalExceptionHandler(LocalizedMessageService messageService) {
        this.messageService = messageService;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String fields = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getField).collect(Collectors.joining(","));
        return ResponseEntity.badRequest().body(ErrorResponse.of("VALIDATION_ERROR", messageService.get("error.validation", fields), req.getRequestURI()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
        HttpStatus status = switch (ex.getCode()) {
            case "AUTH_INVALID_CREDENTIALS", "AUTH_UNAUTHORIZED" -> HttpStatus.UNAUTHORIZED;
            case "AUTH_FORBIDDEN", "RESERVATION_ACCESS_DENIED" -> HttpStatus.FORBIDDEN;
            case "RESERVATION_NOT_FOUND", "USER_NOT_FOUND", "PRODUCT_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "RESERVATION_SEAT_ALREADY_RESERVED" -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };
        return ResponseEntity.status(status).body(ErrorResponse.of(ex.getCode(), messageService.get(ex.getMessage()), req.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.of("AUTH_FORBIDDEN", messageService.get("error.auth.forbidden"), req.getRequestURI()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of("DATA_INTEGRITY", messageService.get("error.data.integrity"), req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("INTERNAL_ERROR", messageService.get("error.internal"), req.getRequestURI()));
    }
}
