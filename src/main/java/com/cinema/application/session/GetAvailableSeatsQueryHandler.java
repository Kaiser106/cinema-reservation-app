package com.cinema.application.session;

import com.cinema.application.common.QueryHandler;
import com.cinema.domain.repository.SeatAvailabilityRepository;
import com.cinema.application.session.queries.GetAvailableSeatsQuery;
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
