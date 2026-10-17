package com.cinema.application.session;

import com.cinema.application.common.QueryHandler;
import com.cinema.application.port.SeatAvailabilityPort;
import com.cinema.application.session.queries.GetAvailableSeatsQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAvailableSeatsQueryHandler implements QueryHandler<GetAvailableSeatsQuery, List<Long>> {
    private final SeatAvailabilityPort seatAvailabilityPort;

    public GetAvailableSeatsQueryHandler(SeatAvailabilityPort seatAvailabilityPort) {
        this.seatAvailabilityPort = seatAvailabilityPort;
    }

    @Override
    public List<Long> handle(GetAvailableSeatsQuery query) {
        return seatAvailabilityPort.findAvailableSeatIds(query.sessionId());
    }
}
