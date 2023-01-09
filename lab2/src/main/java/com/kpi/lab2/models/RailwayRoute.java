package com.kpi.lab2.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class RailwayRoute implements Entity {
    private Long id;
    private RailwayStation startStation;
    private RailwayStation finishStation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Train train;

    @Override
    public Long getId() {
        return id;
    }
}
