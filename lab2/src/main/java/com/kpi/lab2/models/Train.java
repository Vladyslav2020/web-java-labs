package com.kpi.lab2.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Train implements Entity {
    private Long id;
    private Long number;
    private Long numberOfSeats;

    @Override
    public Long getId() {
        return id;
    }
}
