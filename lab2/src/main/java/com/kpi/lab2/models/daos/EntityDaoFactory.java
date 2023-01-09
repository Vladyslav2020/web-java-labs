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
        entityDaoList = Set.of(
                new UserDao(dataSource),
                new TrainDao(dataSource),
                new RailwayStationDao(dataSource),
                new RailwayRouteDao(dataSource),
                new TicketDao(dataSource)
        );
    }

    public <T extends EntityDao<?>> T getEntityDao(Class<T> clazz) {
        Optional<EntityDao<?>> entityDao = entityDaoList.stream().filter(dao -> clazz.equals(dao.getClass())).findFirst();
        return (T) entityDao.orElseThrow(() -> new IllegalArgumentException("Cannot find entity DAO of class: " + clazz.getSimpleName()));
    }
}
