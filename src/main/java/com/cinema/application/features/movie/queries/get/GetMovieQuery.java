package com.cinema.application.features.movie.queries.get;

import com.cinema.application.cqrs.Query;
import com.cinema.application.features.movie.queries.MovieDto;

import java.util.UUID;

public record GetMovieQuery(UUID id) implements Query<MovieDto> {}
