package com.cinema.application.features.session.ports;

import com.cinema.application.features.session.queries.SessionDto;
import com.cinema.domain.session.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface SessionReadRepository {
    Optional<Session> findById(UUID id);
    Page<SessionDto> findSessionDtos(Pageable pageable);
    Optional<SessionDto> findSessionDto(UUID id);
}
