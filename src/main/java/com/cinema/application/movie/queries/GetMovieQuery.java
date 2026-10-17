package com.cinema.application.movie.queries;

import com.cinema.application.common.Query;
import com.cinema.application.movie.dto.MovieDto;

import java.util.UUID;

public record GetMovieQuery(UUID id) implements Query<MovieDto> {}
