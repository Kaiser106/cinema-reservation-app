package com.cinema.presentation.response;

import java.time.Instant;

public record ApiResponse<T>(boolean success, T data, Instant timestamp, String path) {
    public static <T> ApiResponse<T> ok(T data, String path) {
        return new ApiResponse<>(true, data, Instant.now(), path);
    }
}
