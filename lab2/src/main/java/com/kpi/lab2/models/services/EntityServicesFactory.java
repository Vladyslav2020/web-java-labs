package com.kpi.lab2.models.services;

import com.kpi.lab2.models.daos.*;

import java.util.Optional;
import java.util.Set;

public enum EntityServicesFactory {
    INSTANCE;

    private final Set<EntityService<?>> entityServices;

    EntityServicesFactory() {
        entityServices = Set.of(
                new UserService(EntityDaoFactory.INSTANCE.getEntityDao(UserDao.class)),
                new RailwayStationService(EntityDaoFactory.INSTANCE.getEntityDao(RailwayStationDao.class)),
                new RailwayRouteService(EntityDaoFactory.INSTANCE.getEntityDao(RailwayRouteDao.class)),
                new TrainService(EntityDaoFactory.INSTANCE.getEntityDao(TrainDao.class)),
                new TicketService(EntityDaoFactory.INSTANCE.getEntityDao(TicketDao.class))
        );
    }

    public <T extends EntityService<?>> T getEntityService(Class<T> clazz) {
        Optional<EntityService<?>> entityService = entityServices.stream().filter(service -> clazz.equals(service.getClass())).findFirst();
        return (T) entityService.orElseThrow(() -> new IllegalArgumentException("Cannot find the entity service of the class: " + clazz.getSimpleName()));
    }
}
