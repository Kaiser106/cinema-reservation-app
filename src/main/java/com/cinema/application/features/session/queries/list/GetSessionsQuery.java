package com.cinema.application.features.session.queries.list;

import com.cinema.application.cqrs.Query;
import com.cinema.application.features.session.queries.SessionDto;
import org.springframework.data.domain.Page;

public record GetSessionsQuery(int page, int size) implements Query<Page<SessionDto>> {}
