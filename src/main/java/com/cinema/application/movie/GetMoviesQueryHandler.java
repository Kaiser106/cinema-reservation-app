package com.cinema.application.movie;

import com.cinema.application.common.QueryHandler;
import com.cinema.application.movie.dto.MovieDto;
import com.cinema.application.movie.queries.GetMoviesQuery;
import com.cinema.application.port.MovieReadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class GetMoviesQueryHandler implements QueryHandler<GetMoviesQuery, Page<MovieDto>> {
    private final MovieReadRepository movieReadRepository;

    public GetMoviesQueryHandler(MovieReadRepository movieReadRepository) {
        this.movieReadRepository = movieReadRepository;
    }

    @Override
    public Page<MovieDto> handle(GetMoviesQuery query) {
        return movieReadRepository.findMovieDtos(PageRequest.of(query.page(), query.size()));
    }
}
