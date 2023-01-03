package com.kpi.lab2.models.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Ticket implements Entity {
    private Long id;
    private RailwayRoute route;
    private Long seatNumber;
    private User owner;

    @Override
    public Long getId() {
        return id;
    }
}
