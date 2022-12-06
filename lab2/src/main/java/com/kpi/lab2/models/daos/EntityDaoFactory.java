package com.kpi.lab2.models.daos;

import com.kpi.lab2.models.daos.config.DatabaseConnectivityProvider;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Set;

public enum EntityDaoFactory {
    INSTANCE;

    private final Set<EntityDao<?>> entityDaoList;

    EntityDaoFactory() {
        DataSource dataSource = DatabaseConnectivityProvider.INSTANCE.getDataSource();

        UserDao userDao = new UserDao(dataSource);
        TrainDao trainDao = new TrainDao(dataSource);
        RailwayStationDao railwayStationDao = new RailwayStationDao(dataSource);
        RailwayRouteDao railwayRouteDao = new RailwayRouteDao(dataSource, railwayStationDao, trainDao);
        TicketDao ticketDao = new TicketDao(dataSource, userDao, railwayRouteDao);
        entityDaoList = Set.of(
                userDao, trainDao, railwayRouteDao, railwayStationDao, ticketDao
        );
    }

    public <T extends EntityDao<?>> T getEntityDao(Class<T> clazz) {
        Optional<EntityDao<?>> entityDao = entityDaoList.stream().filter(dao -> clazz.equals(dao.getClass())).findFirst();
        return (T) entityDao.orElseThrow(() -> new IllegalArgumentException("Cannot find entity DAO of class: " + clazz.getSimpleName()));
    }
}
