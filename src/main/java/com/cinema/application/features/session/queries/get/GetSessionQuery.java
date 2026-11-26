package com.cinema.application.features.session.queries.get;

import com.cinema.application.cqrs.Query;
import com.cinema.application.features.session.queries.SessionDto;

import java.util.UUID;

public record GetSessionQuery(UUID id) implements Query<SessionDto> {}
