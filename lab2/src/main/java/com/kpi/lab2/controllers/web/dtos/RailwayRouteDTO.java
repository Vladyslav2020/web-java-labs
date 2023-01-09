package com.kpi.lab2.controllers.web.dtos;

import com.kpi.lab2.models.entities.RailwayStation;
import com.kpi.lab2.models.entities.Train;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class RailwayRouteDTO {
    private Long id;
    private RailwayStation startStation;
    private RailwayStation finishStation;
    private LocalDateTime startTime;
    private String duration;
    private LocalDateTime endTime;
    private Train train;
    private Long availableTickets;
}
