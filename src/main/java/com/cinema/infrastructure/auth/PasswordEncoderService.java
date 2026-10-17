package com.cinema.infrastructure.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(String raw) { return encoder.encode(raw); }
    public boolean matches(String raw, String encoded) { return encoder.matches(raw, encoded); }
}
