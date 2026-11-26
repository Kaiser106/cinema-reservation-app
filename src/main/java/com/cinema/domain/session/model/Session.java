package com.cinema.domain.session.model;

import com.cinema.domain.shared.vobj.Money;

import java.time.Instant;
import java.util.UUID;

public class Session {
    private final UUID id;
    private final UUID movieId;
    private final Long hallId;
    private final Instant startTime;
    private final Instant endTime;
    private final Money price;

    public Session(UUID id, UUID movieId, Long hallId, Instant startTime, Instant endTime, Money price) {
        this.id = id;
        this.movieId = movieId;
        this.hallId = hallId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    public UUID getId() { return id; }
    public UUID getMovieId() { return movieId; }
    public Long getHallId() { return hallId; }
    public Instant getStartTime() { return startTime; }
    public Instant getEndTime() { return endTime; }
    public Money getPrice() { return price; }
}
