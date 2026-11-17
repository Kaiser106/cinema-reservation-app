package com.cinema.application.reservation;

import com.cinema.application.common.QueryHandler;
import com.cinema.domain.repository.ReservationRepository;
import com.cinema.application.reservation.dto.ReservationDto;
import com.cinema.application.reservation.queries.GetReservationHistoryQuery;
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
