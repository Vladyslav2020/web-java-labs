package com.kpi.lab2.services;

import com.kpi.lab2.daos.RailwayRouteDao;
import com.kpi.lab2.models.RailwayRoute;
import com.kpi.lab2.models.RailwayStation;

import java.util.List;

public class RailwayRouteService extends EntityServiceBase<RailwayRoute, RailwayRouteDao> {
    public RailwayRouteService(RailwayRouteDao entityDao) {
        super(entityDao);
    }

    public List<RailwayRoute> findByStartAndFinishStations(RailwayStation startStation, RailwayStation finishStation) {
        return entityDao.findByStartStationAndFinishStation(startStation, finishStation);
    }
}
