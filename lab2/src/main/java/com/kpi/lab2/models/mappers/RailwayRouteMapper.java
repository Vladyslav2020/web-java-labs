package com.kpi.lab2.models.mappers;

import com.kpi.lab2.controllers.web.dtos.RailwayRouteDTO;
import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.services.EntityServicesFactory;
import com.kpi.lab2.models.services.TicketService;

import java.time.Duration;
import java.time.LocalDateTime;

public class RailwayRouteMapper {

    private TicketService ticketService;

    public RailwayRouteMapper() {
    }

    public RailwayRouteMapper(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public RailwayRouteDTO mapToDTO(RailwayRoute railwayRoute) {
        return RailwayRouteDTO.builder()
                .id(railwayRoute.getId())
                .startTime(railwayRoute.getStartTime())
                .endTime(railwayRoute.getEndTime())
                .startStation(railwayRoute.getStartStation())
                .finishStation(railwayRoute.getFinishStation())
                .duration(convertToString(railwayRoute.getStartTime(), railwayRoute.getEndTime()))
                .train(railwayRoute.getTrain())
                .availableTickets(railwayRoute.getTrain().getNumberOfSeats() - ticketService.countByRoute(railwayRoute))
                .build();
    }

    private String convertToString(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return duration.getSeconds() / 3600 + "h " + duration.getSeconds() % 3600 / 60 + "m";
    }

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }
}
