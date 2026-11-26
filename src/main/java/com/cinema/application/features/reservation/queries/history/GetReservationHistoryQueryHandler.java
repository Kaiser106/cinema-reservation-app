package com.cinema.application.features.reservation.queries.history;

import com.cinema.application.cqrs.QueryHandler;
import com.cinema.application.features.reservation.queries.ReservationDto;
import com.cinema.domain.reservation.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class GetReservationHistoryQueryHandler implements QueryHandler<GetReservationHistoryQuery, Page<ReservationDto>> {
    private final ReservationRepository reservationRepository;

    public GetReservationHistoryQueryHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Page<ReservationDto> handle(GetReservationHistoryQuery query) {
        return reservationRepository.findByUserId(query.userId(), PageRequest.of(query.page(), query.size()))
                .map(r -> new ReservationDto(r.id(), r.getSessionId(), r.getStatus().name(), r.getTotalPrice().toBigDecimal(), r.getCreatedAt(), r.getExpiresAt(),
                        r.getSeats().stream().map(s -> s.getSeatId()).toList()));
    }
}
