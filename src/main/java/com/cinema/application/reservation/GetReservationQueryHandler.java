package com.cinema.application.reservation;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.ErrorCodes;
import com.cinema.application.common.QueryHandler;
import com.cinema.application.port.ReservationPort;
import com.cinema.application.reservation.dto.ReservationDto;
import com.cinema.application.reservation.queries.GetReservationQuery;
import com.cinema.domain.reservation.Reservation;
import org.springframework.stereotype.Component;

@Component
public class GetReservationQueryHandler implements QueryHandler<GetReservationQuery, ReservationDto> {
    private final ReservationPort reservationPort;

    public GetReservationQueryHandler(ReservationPort reservationPort) {
        this.reservationPort = reservationPort;
    }

    @Override
    public ReservationDto handle(GetReservationQuery query) {
        Reservation r = reservationPort.findById(query.reservationId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.RESERVATION_NOT_FOUND, "error.reservation.notFound"));
        if (!r.getUserId().equals(query.userId())) {
            throw new BusinessException(ErrorCodes.RESERVATION_ACCESS_DENIED, "error.reservation.accessDenied");
        }
        return new ReservationDto(r.id(), r.getSessionId(), r.getStatus().name(), r.getTotalPrice().toBigDecimal(), r.getCreatedAt(), r.getExpiresAt(),
                r.getSeats().stream().map(s -> s.getSeatId()).toList());
    }
}
