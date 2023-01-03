package com.kpi.lab2.models.services;

import com.kpi.lab2.models.daos.*;
import com.kpi.lab2.models.mappers.RailwayRouteMapper;

import java.util.Optional;
import java.util.Set;

public enum EntityServicesFactory {
    INSTANCE;

    private final Set<EntityService<?>> entityServices;

    EntityServicesFactory() {
        RailwayRouteMapper railwayRouteMapper = new RailwayRouteMapper();
        TrainService trainService = new TrainService(EntityDaoFactory.INSTANCE.getEntityDao(TrainDao.class));
        RailwayStationService railwayStationService = new RailwayStationService(EntityDaoFactory.INSTANCE.getEntityDao(RailwayStationDao.class));
        UserService userService = new UserService(EntityDaoFactory.INSTANCE.getEntityDao(UserDao.class));
        RailwayRouteService railwayRouteService = new RailwayRouteService(EntityDaoFactory.INSTANCE.getEntityDao(RailwayRouteDao.class), railwayStationService, trainService, railwayRouteMapper);
        TicketService ticketService = new TicketService(EntityDaoFactory.INSTANCE.getEntityDao(TicketDao.class), userService, railwayRouteService);
        railwayRouteMapper.setTicketService(ticketService);
        entityServices = Set.of(
                userService,
                railwayStationService,
                trainService,
                railwayRouteService,
                ticketService
        );
    }

    public <T extends EntityService<?>> T getEntityService(Class<T> clazz) {
        Optional<EntityService<?>> entityService = entityServices.stream().filter(service -> clazz.equals(service.getClass())).findFirst();
        return (T) entityService.orElseThrow(() -> new IllegalArgumentException("Cannot find the entity service of the class: " + clazz.getSimpleName()));
    }
}
