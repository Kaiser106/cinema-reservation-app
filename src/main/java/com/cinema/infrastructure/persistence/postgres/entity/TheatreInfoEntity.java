package com.cinema.infrastructure.persistence.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "theatre_info")
public class TheatreInfoEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
