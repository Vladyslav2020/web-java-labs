package com.kpi.lab2.models.services;

import com.kpi.lab2.controllers.web.dtos.RailwayRouteDTO;
import com.kpi.lab2.models.daos.RailwayRouteDao;
import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.RailwayStation;
import com.kpi.lab2.models.mappers.RailwayRouteMapper;

import java.util.List;
import java.util.stream.Collectors;

public class RailwayRouteService extends EntityServiceBase<RailwayRoute, RailwayRouteDao> {

    private final RailwayStationService railwayStationService;
    private final TrainService trainService;
    private final RailwayRouteMapper railwayRouteMapper;

    public RailwayRouteService(RailwayRouteDao entityDao, RailwayStationService railwayStationService, TrainService trainService, RailwayRouteMapper railwayRouteMapper) {
        super(entityDao);
        this.railwayStationService = railwayStationService;
        this.trainService = trainService;
        this.railwayRouteMapper = railwayRouteMapper;
    }

    @Override
    public RailwayRoute findById(Long id) {
        return enrichEntity(super.findById(id));
    }

    public List<RailwayRouteDTO> findAllDTO() {
        return entityDao.findAll().stream().map(this::enrichEntity).map(railwayRouteMapper::mapToDTO).collect(Collectors.toList());
    }

    public RailwayRouteDTO findDTOById(Long id) {
        return railwayRouteMapper.mapToDTO(enrichEntity(entityDao.findById(id)));
    }

    public List<RailwayRoute> findByStartAndFinishStations(RailwayStation startStation, RailwayStation finishStation) {
        return entityDao.findByStartStationAndFinishStation(startStation, finishStation).stream().map(this::enrichEntity).collect(Collectors.toList());
    }

    public List<RailwayRouteDTO> findDTOByStartAndFinishStations(RailwayStation startStation, RailwayStation finishStation) {
        return entityDao.findByStartStationAndFinishStation(startStation, finishStation).stream().map(this::enrichEntity).map(railwayRouteMapper::mapToDTO).collect(Collectors.toList());
    }

    private RailwayRoute enrichEntity(RailwayRoute railwayRoute) {
        if (railwayRoute.getStartStation().getId() != null) {
            railwayRoute.setStartStation(railwayStationService.findById(railwayRoute.getStartStation().getId()));
        }
        if (railwayRoute.getFinishStation().getId() != null) {
            railwayRoute.setFinishStation(railwayStationService.findById(railwayRoute.getFinishStation().getId()));
        }
        if (railwayRoute.getTrain().getId() != null) {
            railwayRoute.setTrain(trainService.findById(railwayRoute.getTrain().getId()));
        }
        return railwayRoute;
    }
}
