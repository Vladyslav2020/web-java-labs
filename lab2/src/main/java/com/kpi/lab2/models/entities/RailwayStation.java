package com.kpi.lab2.models.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RailwayStation implements Entity {
    private Long id;
    private String name;

    @Override
    public Long getId() {
        return id;
    }
}
