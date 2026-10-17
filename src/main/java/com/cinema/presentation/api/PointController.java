package com.cinema.presentation.api;

import com.cinema.application.point.GetUserPointsQueryHandler;
import com.cinema.infrastructure.auth.SessionPrincipal;
import com.cinema.presentation.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/points")
public class PointController {
    private final GetUserPointsQueryHandler getUserPointsQueryHandler;

    public PointController(GetUserPointsQueryHandler getUserPointsQueryHandler) {
        this.getUserPointsQueryHandler = getUserPointsQueryHandler;
    }

    @GetMapping
    public ApiResponse<Map<String, Integer>> points(Authentication auth, HttpServletRequest req) {
        SessionPrincipal p = (SessionPrincipal) auth.getPrincipal();
        return ApiResponse.ok(Map.of("points", getUserPointsQueryHandler.handle(p.userId())), req.getRequestURI());
    }
}
