package com.cinema.presentation.api;

import com.cinema.application.movie.GetMovieQueryHandler;
import com.cinema.application.movie.GetMoviesQueryHandler;
import com.cinema.application.movie.dto.MovieDto;
import com.cinema.application.movie.queries.GetMovieQuery;
import com.cinema.application.movie.queries.GetMoviesQuery;
import com.cinema.presentation.response.ApiResponse;
import com.cinema.presentation.response.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final GetMovieQueryHandler getMovieQueryHandler;
    private final GetMoviesQueryHandler getMoviesQueryHandler;

    public MovieController(GetMovieQueryHandler getMovieQueryHandler, GetMoviesQueryHandler getMoviesQueryHandler) {
        this.getMovieQueryHandler = getMovieQueryHandler;
        this.getMoviesQueryHandler = getMoviesQueryHandler;
    }

    @GetMapping
    public ApiResponse<PageResponse<MovieDto>> getMovies(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size,
                                                          HttpServletRequest req) {
        return ApiResponse.ok(PageResponse.of(getMoviesQueryHandler.handle(new GetMoviesQuery(page, size))), req.getRequestURI());
    }

    @GetMapping("/{id}")
    public ApiResponse<MovieDto> getMovie(@PathVariable UUID id, HttpServletRequest req) {
        return ApiResponse.ok(getMovieQueryHandler.handle(new GetMovieQuery(id)), req.getRequestURI());
    }
}
