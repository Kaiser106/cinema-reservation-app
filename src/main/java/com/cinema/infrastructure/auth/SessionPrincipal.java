package com.cinema.infrastructure.auth;

import java.util.UUID;

public record SessionPrincipal(UUID userId, String role, String sessionToken) {}
