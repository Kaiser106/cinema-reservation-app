package com.cinema.application.features.reservation.queries.history;

import com.cinema.application.cqrs.Query;
import com.cinema.application.features.reservation.queries.ReservationDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public record GetReservationHistoryQuery(UUID userId, int page, int size) implements Query<Page<ReservationDto>> {}
