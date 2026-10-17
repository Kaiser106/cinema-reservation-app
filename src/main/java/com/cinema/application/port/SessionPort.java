package com.cinema.application.port;

import com.cinema.domain.session.Session;
import com.cinema.application.session.dto.SessionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface SessionPort {
    Optional<Session> findById(UUID id);
    Page<SessionDto> findSessionDtos(Pageable pageable);
    Optional<SessionDto> findSessionDto(UUID id);
}
