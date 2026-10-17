package com.cinema.domain.hall;

public class Seat {
    private final Long id;
    private final Long hallId;
    private final String rowLabel;
    private final int seatNumber;
    private final SeatType type;

    public Seat(Long id, Long hallId, String rowLabel, int seatNumber, SeatType type) {
        this.id = id;
        this.hallId = hallId;
        this.rowLabel = rowLabel;
        this.seatNumber = seatNumber;
        this.type = type;
    }

    public Long getId() { return id; }
    public Long getHallId() { return hallId; }
    public String getRowLabel() { return rowLabel; }
    public int getSeatNumber() { return seatNumber; }
    public SeatType getType() { return type; }
}
