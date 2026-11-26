package com.cinema.domain.point.model;

import com.cinema.domain.point.events.PointsEarnedEvent;
import com.cinema.domain.shared.AggregateRoot;

import java.time.Instant;
import java.util.UUID;

public class UserPoint extends AggregateRoot<UUID> {
    private final UUID userId;
    private int balance;

    public UserPoint(UUID userId, int balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public void earn(int points) {
        if (points <= 0) throw new IllegalArgumentException("points.invalid");
        balance += points;
        registerEvent(new PointsEarnedEvent(userId, points, Instant.now()));
    }

    public int getBalance() { return balance; }

    @Override
    public UUID id() { return userId; }
}
