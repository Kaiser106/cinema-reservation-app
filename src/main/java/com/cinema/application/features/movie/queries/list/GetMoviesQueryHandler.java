package com.cinema.application.features.movie.queries.list;

import com.cinema.application.cqrs.QueryHandler;
import com.cinema.application.features.movie.ports.MovieReadRepository;
import com.cinema.application.features.movie.queries.MovieDto;
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
