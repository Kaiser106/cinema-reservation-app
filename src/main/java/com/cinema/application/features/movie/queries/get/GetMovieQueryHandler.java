package com.cinema.application.features.movie.queries.get;

import com.cinema.application.cqrs.QueryHandler;
import com.cinema.application.features.movie.ports.MovieReadRepository;
import com.cinema.application.features.movie.queries.MovieDto;
import com.cinema.application.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class GetMovieQueryHandler implements QueryHandler<GetMovieQuery, MovieDto> {
    private final MovieReadRepository movieReadRepository;

    public GetMovieQueryHandler(MovieReadRepository movieReadRepository) {
        this.movieReadRepository = movieReadRepository;
    }

    @Override
    public MovieDto handle(GetMovieQuery query) {
        return movieReadRepository.findMovieDto(query.id()).orElseThrow(() -> new BusinessException("MOVIE_NOT_FOUND", "error.movie.notFound"));
    }
}
