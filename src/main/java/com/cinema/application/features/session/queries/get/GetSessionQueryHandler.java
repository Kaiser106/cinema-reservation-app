package com.cinema.application.features.session.queries.get;

import com.cinema.application.cqrs.QueryHandler;
import com.cinema.application.features.session.ports.SessionReadRepository;
import com.cinema.application.features.session.queries.SessionDto;
import com.cinema.application.shared.exception.BusinessException;
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
