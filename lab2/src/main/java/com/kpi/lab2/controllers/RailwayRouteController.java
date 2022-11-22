package com.kpi.lab2.controllers;

import com.kpi.lab2.models.RailwayRoute;
import com.kpi.lab2.services.RailwayRouteService;

public class RailwayRouteController {
    private RailwayRouteService railwayRouteService;

    public RailwayRoute createRailwayRoute(RailwayRoute railwayRoute) {
        return railwayRouteService.create(railwayRoute);
    }
}
