package com.cinema.application.movie;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.QueryHandler;
import com.cinema.application.movie.dto.MovieDto;
import com.cinema.application.movie.queries.GetMovieQuery;
import com.cinema.application.port.MoviePort;
import org.springframework.stereotype.Component;

@Component
public class GetMovieQueryHandler implements QueryHandler<GetMovieQuery, MovieDto> {
    private final MoviePort moviePort;

    public GetMovieQueryHandler(MoviePort moviePort) {
        this.moviePort = moviePort;
    }

    @Override
    public MovieDto handle(GetMovieQuery query) {
        return moviePort.findMovieDto(query.id()).orElseThrow(() -> new BusinessException("MOVIE_NOT_FOUND", "error.movie.notFound"));
    }
}
