package com.cinema.application.features.session.queries.availableseats;

import com.cinema.application.cqrs.Query;

import java.util.List;
import java.util.UUID;

public record GetAvailableSeatsQuery(UUID sessionId) implements Query<List<Long>> {}
