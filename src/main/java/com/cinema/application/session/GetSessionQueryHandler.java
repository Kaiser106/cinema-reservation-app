package com.cinema.application.session;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.QueryHandler;
import com.cinema.application.port.SessionReadRepository;
import com.cinema.application.session.dto.SessionDto;
import com.cinema.application.session.queries.GetSessionQuery;
import org.springframework.stereotype.Component;

@Component
public class GetSessionQueryHandler implements QueryHandler<GetSessionQuery, SessionDto> {
    private final SessionReadRepository sessionReadRepository;

    public GetSessionQueryHandler(SessionReadRepository sessionReadRepository) {
        this.sessionReadRepository = sessionReadRepository;
    }

    @Override
    public SessionDto handle(GetSessionQuery query) {
        return sessionReadRepository.findSessionDto(query.id()).orElseThrow(() -> new BusinessException("SESSION_NOT_FOUND", "error.session.notFound"));
    }
}
