package com.cinema.application.point;

import com.cinema.domain.repository.UserPointRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetUserPointsQueryHandler {
    private final UserPointRepository userPointRepository;

    public GetUserPointsQueryHandler(UserPointRepository userPointRepository) {
        this.userPointRepository = userPointRepository;
    }

    public int handle(UUID userId) {
        return userPointRepository.findByUserId(userId).map(p -> p.getBalance()).orElse(0);
    }
}
