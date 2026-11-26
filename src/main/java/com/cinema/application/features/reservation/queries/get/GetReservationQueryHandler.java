package com.cinema.application.features.reservation.queries.get;

import com.cinema.application.cqrs.QueryHandler;
import com.cinema.application.features.reservation.queries.ReservationDto;
import com.cinema.application.shared.exception.BusinessException;
import com.cinema.application.shared.exception.ErrorCodes;
import com.cinema.domain.reservation.model.Reservation;
import com.cinema.domain.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Component;

@Component
public class GetReservationQueryHandler implements QueryHandler<GetReservationQuery, ReservationDto> {
    private final ReservationRepository reservationRepository;

    public GetReservationQueryHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public ReservationDto handle(GetReservationQuery query) {
        Reservation r = reservationRepository.findById(query.reservationId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.RESERVATION_NOT_FOUND, "error.reservation.notFound"));
        if (!r.getUserId().equals(query.userId())) {
            throw new BusinessException(ErrorCodes.RESERVATION_ACCESS_DENIED, "error.reservation.accessDenied");
        }
        return new ReservationDto(r.id(), r.getSessionId(), r.getStatus().name(), r.getTotalPrice().toBigDecimal(), r.getCreatedAt(), r.getExpiresAt(),
                r.getSeats().stream().map(s -> s.getSeatId()).toList());
    }
}
