package com.cinema.infrastructure.persistence.postgres.adapter;

import com.cinema.application.port.SessionPort;
import com.cinema.application.session.dto.SessionDto;
import com.cinema.domain.common.Money;
import com.cinema.domain.session.Session;
import com.cinema.infrastructure.persistence.postgres.repository.SessionJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SessionPersistenceAdapter implements SessionPort {
    private final SessionJpaRepository repo;

    public SessionPersistenceAdapter(SessionJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return repo.findById(id).map(e -> new Session(e.getId(), e.getMovieId(), e.getHallId(), e.getStartTime(), e.getEndTime(), Money.of(e.getPrice())));
    }

    @Override
    public Page<SessionDto> findSessionDtos(Pageable pageable) {
        return repo.findSessionDtos(pageable);
    }

    @Override
    public Optional<SessionDto> findSessionDto(UUID id) {
        return repo.findSessionDto(id);
    }
}
