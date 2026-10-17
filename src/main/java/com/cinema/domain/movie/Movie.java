package com.cinema.domain.movie;

import java.time.Instant;
import java.util.UUID;

public class Movie {
    private final UUID id;
    private final String title;
    private final int durationMinutes;
    private final String genre;
    private final String rating;
    private final String posterUrl;
    private final Instant createdAt;

    public Movie(UUID id, String title, int durationMinutes, String genre, String rating, String posterUrl, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
        this.rating = rating;
        this.posterUrl = posterUrl;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getGenre() { return genre; }
    public String getRating() { return rating; }
    public String getPosterUrl() { return posterUrl; }
    public Instant getCreatedAt() { return createdAt; }
}
