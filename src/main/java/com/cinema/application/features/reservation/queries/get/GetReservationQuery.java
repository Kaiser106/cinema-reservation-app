package com.cinema.application.features.reservation.queries.get;

import com.cinema.application.cqrs.Query;
import com.cinema.application.features.reservation.queries.ReservationDto;

import java.util.UUID;

public record GetReservationQuery(UUID reservationId, UUID userId) implements Query<ReservationDto> {}
