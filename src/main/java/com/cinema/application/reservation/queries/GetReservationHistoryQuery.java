package com.cinema.application.reservation.queries;

import com.cinema.application.common.Query;
import com.cinema.application.reservation.dto.ReservationDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public record GetReservationHistoryQuery(UUID userId, int page, int size) implements Query<Page<ReservationDto>> {}
