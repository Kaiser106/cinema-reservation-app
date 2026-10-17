package com.cinema.application.session;

import com.cinema.application.common.QueryHandler;
import com.cinema.application.port.SessionPort;
import com.cinema.application.session.dto.SessionDto;
import com.cinema.application.session.queries.GetSessionsQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class GetSessionsQueryHandler implements QueryHandler<GetSessionsQuery, Page<SessionDto>> {
    private final SessionPort sessionPort;

    public GetSessionsQueryHandler(SessionPort sessionPort) {
        this.sessionPort = sessionPort;
    }

    @Override
    public Page<SessionDto> handle(GetSessionsQuery query) {
        return sessionPort.findSessionDtos(PageRequest.of(query.page(), query.size()));
    }
}
