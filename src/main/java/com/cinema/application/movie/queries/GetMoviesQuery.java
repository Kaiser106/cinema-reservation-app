package com.cinema.application.movie.queries;

import com.cinema.application.common.Query;
import com.cinema.application.movie.dto.MovieDto;
import org.springframework.data.domain.Page;

public record GetMoviesQuery(int page, int size) implements Query<Page<MovieDto>> {}
