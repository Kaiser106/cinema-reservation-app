package com.cinema.domain.hall;

public class Hall {
    private final Long id;
    private final String name;
    private final int rowCount;

    public Hall(Long id, String name, int rowCount) {
        this.id = id;
        this.name = name;
        this.rowCount = rowCount;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getRowCount() { return rowCount; }
}
