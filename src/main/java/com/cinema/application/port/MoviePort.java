package com.cinema.application.port;

import com.cinema.application.movie.dto.MovieDto;
import com.cinema.domain.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MoviePort {
    Optional<Movie> findById(UUID id);
    Optional<MovieDto> findMovieDto(UUID id);
    Page<MovieDto> findMovieDtos(Pageable pageable);
}
