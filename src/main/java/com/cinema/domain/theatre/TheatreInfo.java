package com.cinema.domain.theatre;

import java.util.UUID;

public class TheatreInfo {
    private final UUID id;
    private final String name;
    private final String address;

    public TheatreInfo(UUID id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
}
