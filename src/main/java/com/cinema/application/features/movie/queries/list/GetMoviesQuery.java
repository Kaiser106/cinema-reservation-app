package com.cinema.application.features.movie.queries.list;

import com.cinema.application.cqrs.Query;
import com.cinema.application.features.movie.queries.MovieDto;
import org.springframework.data.domain.Page;

public record GetMoviesQuery(int page, int size) implements Query<Page<MovieDto>> {}
