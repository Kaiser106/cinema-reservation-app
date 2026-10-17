package com.cinema.presentation.api;

import com.cinema.application.session.GetAvailableSeatsQueryHandler;
import com.cinema.application.session.GetSessionQueryHandler;
import com.cinema.application.session.GetSessionsQueryHandler;
import com.cinema.application.session.dto.SessionDto;
import com.cinema.application.session.queries.GetAvailableSeatsQuery;
import com.cinema.application.session.queries.GetSessionQuery;
import com.cinema.application.session.queries.GetSessionsQuery;
import com.cinema.presentation.response.ApiResponse;
import com.cinema.presentation.response.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final GetSessionsQueryHandler sessionsQueryHandler;
    private final GetSessionQueryHandler sessionQueryHandler;
    private final GetAvailableSeatsQueryHandler availableSeatsQueryHandler;

    public SessionController(GetSessionsQueryHandler sessionsQueryHandler, GetSessionQueryHandler sessionQueryHandler, GetAvailableSeatsQueryHandler availableSeatsQueryHandler) {
        this.sessionsQueryHandler = sessionsQueryHandler;
        this.sessionQueryHandler = sessionQueryHandler;
        this.availableSeatsQueryHandler = availableSeatsQueryHandler;
    }

    @GetMapping
    public ApiResponse<PageResponse<SessionDto>> getSessions(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size,
                                                              HttpServletRequest req) {
        return ApiResponse.ok(PageResponse.of(sessionsQueryHandler.handle(new GetSessionsQuery(page, size))), req.getRequestURI());
    }

    @GetMapping("/{id}")
    public ApiResponse<SessionDto> getSession(@PathVariable UUID id, HttpServletRequest req) {
        return ApiResponse.ok(sessionQueryHandler.handle(new GetSessionQuery(id)), req.getRequestURI());
    }

    @GetMapping("/{id}/available-seats")
    public ApiResponse<Map<String, List<Long>>> availableSeats(@PathVariable UUID id, HttpServletRequest req) {
        return ApiResponse.ok(Map.of("seatIds", availableSeatsQueryHandler.handle(new GetAvailableSeatsQuery(id))), req.getRequestURI());
    }
}
