package com.cinema.infrastructure.persistence.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sessions")
public class SessionEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "movie_id", nullable = false, columnDefinition = "uuid")
    private UUID movieId;

    @Column(name = "hall_id", nullable = false)
    private Long hallId;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getMovieId() { return movieId; }
    public void setMovieId(UUID movieId) { this.movieId = movieId; }
    public Long getHallId() { return hallId; }
    public void setHallId(Long hallId) { this.hallId = hallId; }
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }
    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant endTime) { this.endTime = endTime; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
