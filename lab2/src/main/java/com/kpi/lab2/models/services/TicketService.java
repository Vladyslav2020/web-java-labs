package com.kpi.lab2.models.services;

import com.kpi.lab2.models.daos.TicketDao;
import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.Ticket;
import com.kpi.lab2.models.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class TicketService extends EntityServiceBase<Ticket, TicketDao> {
    private final UserService userService;
    private final RailwayRouteService railwayRouteService;

    public TicketService(TicketDao entityDao, UserService userService, RailwayRouteService railwayRouteService) {
        super(entityDao);
        this.userService = userService;
        this.railwayRouteService = railwayRouteService;
    }

    @Override
    public Ticket findById(Long id) {
        return enrichEntity(super.findById(id));
    }

    public List<Ticket> findByRoute(RailwayRoute railwayRoute) {
        return entityDao.findByRoute(railwayRoute).stream().map(this::enrichEntity).collect(Collectors.toList());
    }

    public List<Ticket> findByOwner(User user) {
        return entityDao.findByOwner(user).stream().map(this::enrichEntity).collect(Collectors.toList());
    }

    public <T extends RailwayRoute> Long countByRoute(T obj) {
        return entityDao.countByRoute(obj);
    }

    private Ticket enrichEntity(Ticket ticket) {
        if (ticket.getRoute().getId() != null) {
            ticket.setRoute(railwayRouteService.findById(ticket.getRoute().getId()));
        }
        if (ticket.getOwner().getId() != null) {
            ticket.setOwner(userService.findById(ticket.getOwner().getId()));
        }
        return ticket;
    }
}
