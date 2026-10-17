package com.cinema.infrastructure.persistence.postgres.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "movies")
public class MovieEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private String rating;
    @Column(name = "poster_url")
    private String posterUrl;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}
