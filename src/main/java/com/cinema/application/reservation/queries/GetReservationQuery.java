package com.cinema.application.reservation.queries;

import com.cinema.application.common.Query;
import com.cinema.application.reservation.dto.ReservationDto;

import java.util.UUID;

public record GetReservationQuery(UUID reservationId, UUID userId) implements Query<ReservationDto> {}
