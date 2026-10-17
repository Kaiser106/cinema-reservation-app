package com.cinema.application.session.queries;

import com.cinema.application.common.Query;
import com.cinema.application.session.dto.SessionDto;
import org.springframework.data.domain.Page;

public record GetSessionsQuery(int page, int size) implements Query<Page<SessionDto>> {}
