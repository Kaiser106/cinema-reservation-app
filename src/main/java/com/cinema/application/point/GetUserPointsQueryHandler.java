package com.cinema.application.point;

import com.cinema.application.port.UserPointPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetUserPointsQueryHandler {
    private final UserPointPort userPointPort;

    public GetUserPointsQueryHandler(UserPointPort userPointPort) {
        this.userPointPort = userPointPort;
    }

    public int handle(UUID userId) {
        return userPointPort.findByUserId(userId).map(p -> p.getBalance()).orElse(0);
    }
}
