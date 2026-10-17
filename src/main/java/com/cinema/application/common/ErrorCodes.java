package com.cinema.application.common;

public final class ErrorCodes {
    private ErrorCodes() {}
    public static final String AUTH_INVALID_CREDENTIALS = "AUTH_INVALID_CREDENTIALS";
    public static final String AUTH_UNAUTHORIZED = "AUTH_UNAUTHORIZED";
    public static final String AUTH_FORBIDDEN = "AUTH_FORBIDDEN";
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String RESERVATION_NOT_FOUND = "RESERVATION_NOT_FOUND";
    public static final String RESERVATION_SEAT_ALREADY_RESERVED = "RESERVATION_SEAT_ALREADY_RESERVED";
    public static final String RESERVATION_ACCESS_DENIED = "RESERVATION_ACCESS_DENIED";
    public static final String PAYMENT_IDEMPOTENT_REPLAY = "PAYMENT_IDEMPOTENT_REPLAY";
    public static final String PAYMENT_FAILED = "PAYMENT_FAILED";
    public static final String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";
}
