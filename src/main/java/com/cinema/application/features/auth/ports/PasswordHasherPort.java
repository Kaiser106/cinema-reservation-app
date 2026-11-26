package com.cinema.application.features.auth.ports;

public interface PasswordHasherPort {
    String encode(String raw);
    boolean matches(String raw, String encoded);
}
