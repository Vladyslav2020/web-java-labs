package com.kpi.lab2.services;

import com.kpi.lab2.daos.TicketDao;
import com.kpi.lab2.models.RailwayRoute;
import com.kpi.lab2.models.Ticket;
import com.kpi.lab2.models.User;

import java.util.List;

public class TicketService extends EntityServiceBase<Ticket, TicketDao> {
    public TicketService(TicketDao entityDao) {
        super(entityDao);
    }

    public List<Ticket> findByRoute(RailwayRoute railwayRoute) {
        return entityDao.findByRoute(railwayRoute);
    }

    public List<Ticket> findByOwner(User user) {
        return entityDao.findByOwner(user);
    }

    public <T extends RailwayRoute> Long countByRoute(T obj) {
        return entityDao.countByRoute(obj);
    }
}
