package com.cinema.application.features.session.queries.list;

import com.cinema.application.cqrs.QueryHandler;
import com.cinema.application.features.session.ports.SessionReadRepository;
import com.cinema.application.features.session.queries.SessionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class GetSessionsQueryHandler implements QueryHandler<GetSessionsQuery, Page<SessionDto>> {
    private final SessionReadRepository sessionReadRepository;

    public GetSessionsQueryHandler(SessionReadRepository sessionReadRepository) {
        this.sessionReadRepository = sessionReadRepository;
    }

    @Override
    public Page<SessionDto> handle(GetSessionsQuery query) {
        return sessionReadRepository.findSessionDtos(PageRequest.of(query.page(), query.size()));
    }
}
