package com.cinema.infrastructure.auth;

import com.cinema.application.features.auth.ports.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService implements PasswordHasherPort {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(String raw) { return encoder.encode(raw); }
    public boolean matches(String raw, String encoded) { return encoder.matches(raw, encoded); }
}
