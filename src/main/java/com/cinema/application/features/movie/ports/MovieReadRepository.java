package com.cinema.application.features.movie.ports;

import com.cinema.application.features.movie.queries.MovieDto;
import com.cinema.domain.movie.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MovieReadRepository {
    Optional<Movie> findById(UUID id);
    Optional<MovieDto> findMovieDto(UUID id);
    Page<MovieDto> findMovieDtos(Pageable pageable);
}
