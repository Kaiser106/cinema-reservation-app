package com.cinema.application.port;

import com.cinema.domain.point.UserPoint;

import java.util.Optional;
import java.util.UUID;

public interface UserPointPort {
    Optional<UserPoint> findByUserId(UUID userId);
    UserPoint save(UserPoint point);
}
