package com.kpi.lab2.services;

import com.kpi.lab2.daos.RailwayStationDao;
import com.kpi.lab2.models.RailwayStation;

import java.util.List;

public class RailwayStationService extends EntityServiceBase<RailwayStation, RailwayStationDao> {
    public RailwayStationService(RailwayStationDao entityDao) {
        super(entityDao);
    }

    public List<RailwayStation> findByName(String name) {
        return entityDao.findByName(name);
    }
}