package com.cinema.application.port;

public interface PasswordHasherPort {
    String encode(String raw);
    boolean matches(String raw, String encoded);
}
