package com.cinema.presentation.api;

import com.cinema.application.reservation.*;
import com.cinema.application.reservation.commands.CancelReservationCommand;
import com.cinema.application.reservation.commands.ConfirmReservationCommand;
import com.cinema.application.reservation.commands.CreateReservationCommand;
import com.cinema.application.reservation.dto.ReservationDto;
import com.cinema.application.reservation.queries.GetReservationHistoryQuery;
import com.cinema.application.reservation.queries.GetReservationQuery;
import com.cinema.infrastructure.auth.SessionPrincipal;
import com.cinema.presentation.request.reservation.CreateReservationRequest;
import com.cinema.presentation.response.ApiResponse;
import com.cinema.presentation.response.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final CreateReservationCommandHandler createHandler;
    private final ConfirmReservationCommandHandler confirmHandler;
    private final CancelReservationCommandHandler cancelHandler;
    private final GetReservationQueryHandler getReservationQueryHandler;
    private final GetReservationHistoryQueryHandler getReservationHistoryQueryHandler;

    public ReservationController(CreateReservationCommandHandler createHandler,
                                 ConfirmReservationCommandHandler confirmHandler,
                                 CancelReservationCommandHandler cancelHandler,
                                 GetReservationQueryHandler getReservationQueryHandler,
                                 GetReservationHistoryQueryHandler getReservationHistoryQueryHandler) {
        this.createHandler = createHandler;
        this.confirmHandler = confirmHandler;
        this.cancelHandler = cancelHandler;
        this.getReservationQueryHandler = getReservationQueryHandler;
        this.getReservationHistoryQueryHandler = getReservationHistoryQueryHandler;
    }

    @PostMapping
    public ApiResponse<Map<String, UUID>> create(@Valid @RequestBody CreateReservationRequest request, Authentication auth, HttpServletRequest req) {
        SessionPrincipal p = (SessionPrincipal) auth.getPrincipal();
        UUID id = createHandler.handle(new CreateReservationCommand(p.userId(), request.sessionId(), request.seatIds()));
        return ApiResponse.ok(Map.of("reservationId", id), req.getRequestURI());
    }

    @GetMapping("/{id}")
    public ApiResponse<ReservationDto> get(@PathVariable UUID id, Authentication auth, HttpServletRequest req) {
        SessionPrincipal p = (SessionPrincipal) auth.getPrincipal();
        return ApiResponse.ok(getReservationQueryHandler.handle(new GetReservationQuery(id, p.userId())), req.getRequestURI());
    }

    @GetMapping("/my")
    public ApiResponse<PageResponse<ReservationDto>> my(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size,
                                                        Authentication auth,
                                                        HttpServletRequest req) {
        SessionPrincipal p = (SessionPrincipal) auth.getPrincipal();
        return ApiResponse.ok(PageResponse.of(getReservationHistoryQueryHandler.handle(new GetReservationHistoryQuery(p.userId(), page, size))), req.getRequestURI());
    }

    @PostMapping("/{id}/confirm")
    public ApiResponse<Void> confirm(@PathVariable UUID id, Authentication auth, HttpServletRequest req) {
        SessionPrincipal p = (SessionPrincipal) auth.getPrincipal();
        confirmHandler.handle(new ConfirmReservationCommand(id, p.userId()));
        return ApiResponse.ok(null, req.getRequestURI());
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable UUID id, @RequestParam(defaultValue = "user_request") String reason, Authentication auth, HttpServletRequest req) {
        SessionPrincipal p = (SessionPrincipal) auth.getPrincipal();
        cancelHandler.handle(new CancelReservationCommand(id, p.userId(), reason));
        return ApiResponse.ok(null, req.getRequestURI());
    }
}
