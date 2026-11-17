package com.cinema.application.session;

import com.cinema.application.common.QueryHandler;
import com.cinema.application.port.SessionReadRepository;
import com.cinema.application.session.dto.SessionDto;
import com.cinema.application.session.queries.GetSessionsQuery;
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
