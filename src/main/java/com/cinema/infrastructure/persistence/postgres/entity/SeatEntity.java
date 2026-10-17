package com.cinema.infrastructure.persistence.postgres.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_label", nullable = false, length = 10)
    private String rowLabel;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "hall_id", nullable = false)
    private Long hallId;

    @Column(nullable = false, length = 20)
    private String type;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRowLabel() { return rowLabel; }
    public void setRowLabel(String rowLabel) { this.rowLabel = rowLabel; }
    public Integer getSeatNumber() { return seatNumber; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }
    public Long getHallId() { return hallId; }
    public void setHallId(Long hallId) { this.hallId = hallId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
