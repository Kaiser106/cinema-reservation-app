package com.cinema.application.session;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.QueryHandler;
import com.cinema.application.port.SessionPort;
import com.cinema.application.session.dto.SessionDto;
import com.cinema.application.session.queries.GetSessionQuery;
import org.springframework.stereotype.Component;

@Component
public class GetSessionQueryHandler implements QueryHandler<GetSessionQuery, SessionDto> {
    private final SessionPort sessionPort;

    public GetSessionQueryHandler(SessionPort sessionPort) {
        this.sessionPort = sessionPort;
    }

    @Override
    public SessionDto handle(GetSessionQuery query) {
        return sessionPort.findSessionDto(query.id()).orElseThrow(() -> new BusinessException("SESSION_NOT_FOUND", "error.session.notFound"));
    }
}
