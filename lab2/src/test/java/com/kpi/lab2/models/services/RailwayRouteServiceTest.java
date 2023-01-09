package com.kpi.lab2.models.services;

import com.kpi.lab2.controllers.web.dtos.RailwayRouteDTO;
import com.kpi.lab2.models.daos.RailwayRouteDao;
import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.RailwayStation;
import com.kpi.lab2.models.mappers.RailwayRouteMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.kpi.lab2.models.services.RailwayStationServiceTest.railwayStations;
import static com.kpi.lab2.models.services.TrainServiceTest.trains;
import static org.mockito.ArgumentMatchers.any;

class RailwayRouteServiceTest {

    public static List<RailwayRoute> railwayRoutes = List.of(
            RailwayRoute.builder()
                    .id(1L)
                    .train(trains.get(0))
                    .startStation(railwayStations.get(0))
                    .finishStation(railwayStations.get(1))
                    .startTime(LocalDateTime.now())
                    .endTime(LocalDateTime.now())
                    .build(),
            RailwayRoute.builder()
                    .id(2L)
                    .train(trains.get(1))
                    .startStation(railwayStations.get(1))
                    .finishStation(railwayStations.get(2))
                    .startTime(LocalDateTime.now())
                    .endTime(LocalDateTime.now())
                    .build(),
            RailwayRoute.builder()
                    .id(3L)
                    .train(trains.get(2))
                    .startStation(railwayStations.get(2))
                    .finishStation(railwayStations.get(3))
                    .startTime(LocalDateTime.now())
                    .endTime(LocalDateTime.now())
                    .build()
    );

    private RailwayRouteService railwayRouteService;

    @BeforeEach
    public void init() {
        RailwayRouteDao railwayRouteDao = Mockito.mock(RailwayRouteDao.class);

        RailwayStationService railwayStationService = Mockito.mock(RailwayStationService.class);

        TrainService trainService = Mockito.mock(TrainService.class);

        TicketService ticketService = Mockito.mock(TicketService.class);

        RailwayRouteMapper railwayRouteMapper = new RailwayRouteMapper(ticketService);

        railwayRouteService = new RailwayRouteService(railwayRouteDao, railwayStationService, trainService, railwayRouteMapper);

        Mockito.doReturn(railwayRoutes).when(railwayRouteDao).findAll();

        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0, Long.class);
            return railwayRoutes.stream().filter(railwayRoute -> railwayRoute.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
        }).when(railwayRouteDao).findById(any(Long.class));

        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0, Long.class);
            return railwayStations.stream().filter(railwayStation -> railwayStation.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
        }).when(railwayStationService).findById(any(Long.class));

        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0, Long.class);
            return trains.stream().filter(train -> train.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
        }).when(trainService).findById(any(Long.class));

        Mockito.doAnswer(invocation -> {
            RailwayStation startRailwayStation = invocation.getArgument(0, RailwayStation.class);
            RailwayStation finishRailwayStation = invocation.getArgument(1, RailwayStation.class);
            return railwayRoutes.stream().filter(railwayRoute -> railwayRoute.getStartStation().equals(startRailwayStation) && railwayRoute.getFinishStation().equals(finishRailwayStation)).collect(Collectors.toList());
        }).when(railwayRouteDao).findByStartStationAndFinishStation(any(RailwayStation.class), any(RailwayStation.class));
    }

    @Test
    public void findAllDTO() {
        List<RailwayRouteDTO> actualResult = railwayRouteService.findAllDTO();
        Assertions.assertEquals(3, actualResult.size());
        for (int i = 0; i < actualResult.size(); i++) {
            Assertions.assertEquals(railwayRoutes.get(i).getId(), actualResult.get(i).getId());
        }
    }

    @Test
    public void findDTOById() {
        RailwayRoute actualRailwayRoute = railwayRouteService.findById(1L);
        Assertions.assertEquals(railwayRoutes.get(0).getId(), actualRailwayRoute.getId());
        Assertions.assertThrows(NoSuchElementException.class, () -> railwayRouteService.findById(10L));
    }

    @Test
    public void findDTOByStartAndFinishStations() {
        List<RailwayRouteDTO> actualRailwayRoutes = railwayRouteService.findDTOByStartAndFinishStations(railwayStations.get(0), railwayStations.get(1));
        Assertions.assertEquals(1, actualRailwayRoutes.size());
        Assertions.assertEquals(1L, actualRailwayRoutes.get(0).getId());
    }
}