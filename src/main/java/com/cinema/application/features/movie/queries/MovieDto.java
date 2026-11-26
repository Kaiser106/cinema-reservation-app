package com.cinema.application.features.movie.queries;

import java.util.UUID;

public record MovieDto(UUID id, String title, int durationMinutes, String genre, String rating, String posterUrl) {}
