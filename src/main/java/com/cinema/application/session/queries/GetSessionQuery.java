package com.cinema.application.session.queries;

import com.cinema.application.common.Query;
import com.cinema.application.session.dto.SessionDto;

import java.util.UUID;

public record GetSessionQuery(UUID id) implements Query<SessionDto> {}
