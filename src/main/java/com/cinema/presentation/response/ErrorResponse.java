package com.cinema.presentation.response;

import java.time.Instant;

public record ErrorResponse(boolean success, String code, String message, Instant timestamp, String path) {
    public static ErrorResponse of(String code, String message, String path) {
        return new ErrorResponse(false, code, message, Instant.now(), path);
    }
}
