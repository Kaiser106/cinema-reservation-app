package com.cinema.application.features.session.queries.availableseats;

import com.cinema.application.cqrs.QueryHandler;
import com.cinema.domain.reservation.repository.SeatAvailabilityRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAvailableSeatsQueryHandler implements QueryHandler<GetAvailableSeatsQuery, List<Long>> {
    private final SeatAvailabilityRepository seatAvailabilityRepository;

    public GetAvailableSeatsQueryHandler(SeatAvailabilityRepository seatAvailabilityRepository) {
        this.seatAvailabilityRepository = seatAvailabilityRepository;
    }

    @Override
    public List<Long> handle(GetAvailableSeatsQuery query) {
        return seatAvailabilityRepository.findAvailableSeatIds(query.sessionId());
    }
}
