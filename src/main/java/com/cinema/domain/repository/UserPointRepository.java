package com.cinema.domain.repository;

import com.cinema.domain.point.UserPoint;

import java.util.Optional;
import java.util.UUID;

public interface UserPointRepository {
    Optional<UserPoint> findByUserId(UUID userId);
    UserPoint save(UserPoint point);
}
