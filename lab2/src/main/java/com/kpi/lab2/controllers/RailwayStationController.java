package com.kpi.lab2.controllers;

import com.kpi.lab2.models.RailwayStation;
import com.kpi.lab2.services.RailwayStationService;

public class RailwayStationController {
    private RailwayStationService railwayStationService;

    public RailwayStation createRailwayStation(RailwayStation railwayStation) {
        return railwayStationService.create(railwayStation);
    }
}
