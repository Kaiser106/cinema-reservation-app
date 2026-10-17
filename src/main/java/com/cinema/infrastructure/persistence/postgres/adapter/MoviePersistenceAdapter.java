package com.cinema.infrastructure.persistence.postgres.adapter;

import com.cinema.application.movie.dto.MovieDto;
import com.cinema.application.port.MoviePort;
import com.cinema.domain.movie.Movie;
import com.cinema.infrastructure.persistence.postgres.repository.MovieJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class MoviePersistenceAdapter implements MoviePort {
    private final MovieJpaRepository repo;

    public MoviePersistenceAdapter(MovieJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Movie> findById(UUID id) {
        return repo.findById(id).map(e -> new Movie(e.getId(), e.getTitle(), e.getDurationMinutes(), e.getGenre(), e.getRating(), e.getPosterUrl(), Instant.now()));
    }

    @Override
    public Optional<MovieDto> findMovieDto(UUID id) { return repo.findMovieDtoById(id); }

    @Override
    public Page<MovieDto> findMovieDtos(Pageable pageable) { return repo.findMovieDtos(pageable); }
}
