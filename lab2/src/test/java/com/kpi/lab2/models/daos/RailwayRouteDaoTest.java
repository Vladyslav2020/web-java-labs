package com.kpi.lab2.models.daos;

import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.RailwayStation;
import com.kpi.lab2.models.entities.Train;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

class RailwayRouteDaoTest {

    private final RailwayRouteDao railwayRouteDao = new RailwayRouteDao(EntityDaoTest.getDataSource());

    @Test
    public void readRailwayRoutes() {
        List<RailwayRoute> railwayRoutes = railwayRouteDao.findAll().stream().filter(railwayRoute -> railwayRoute.getId() <= 5).toList();
        Assertions.assertEquals(5, railwayRoutes.size());
        for (RailwayRoute railwayRoute : railwayRoutes) {
            RailwayRoute railwayRouteById = railwayRouteDao.findById(railwayRoute.getId());
            compareRailwayRoutes(railwayRoute, railwayRouteById);
        }
    }

    private void compareRailwayRoutes(RailwayRoute expectedRailwayRoute, RailwayRoute actualRailwayRoute) {
        Assertions.assertEquals(expectedRailwayRoute.getId(), actualRailwayRoute.getId());
        Assertions.assertEquals(expectedRailwayRoute.getStartTime(), actualRailwayRoute.getStartTime());
        Assertions.assertEquals(expectedRailwayRoute.getEndTime(), actualRailwayRoute.getEndTime());
        Assertions.assertEquals(expectedRailwayRoute.getStartStation().getId(), actualRailwayRoute.getStartStation().getId());
        Assertions.assertEquals(expectedRailwayRoute.getFinishStation().getId(), actualRailwayRoute.getFinishStation().getId());
        Assertions.assertEquals(expectedRailwayRoute.getTrain().getId(), actualRailwayRoute.getTrain().getId());
    }

    @Test
    public void createRailwayRoute() {
        LocalDateTime time = LocalDateTime.now();
        RailwayRoute railwayRoute = railwayRouteDao.save(getRailwayRoute(time));
        Assertions.assertNotNull(railwayRoute.getId());
        Assertions.assertEquals(time, railwayRoute.getStartTime());
        Assertions.assertEquals(time, railwayRoute.getEndTime());
        Assertions.assertEquals(1L, railwayRoute.getStartStation().getId());
        Assertions.assertEquals(2L, railwayRoute.getFinishStation().getId());
        Assertions.assertEquals(1L, railwayRoute.getTrain().getId());
    }

    @Test
    public void updateRailwayRoute() {
        LocalDateTime time1 = LocalDateTime.of(2000, 1, 1, 12, 0);
        RailwayRoute railwayRoute = getRailwayRoute(time1);
        railwayRoute = railwayRouteDao.save(railwayRoute);
        Assertions.assertNotNull(railwayRoute.getId());
        railwayRoute.setTrain(Train.builder().id(2L).build());
        railwayRoute.setStartStation(RailwayStation.builder().id(2L).build());
        railwayRoute.setFinishStation(RailwayStation.builder().id(1L).build());
        railwayRoute.setTrain(Train.builder().id(2L).build());
        LocalDateTime time2 = LocalDateTime.now();
        railwayRoute.setStartTime(time2);
        railwayRoute.setEndTime(time2);
        RailwayRoute updatedRailwayRoute = railwayRouteDao.save(railwayRoute);
        Assertions.assertEquals(railwayRoute.getId(), updatedRailwayRoute.getId());
        Assertions.assertEquals(railwayRoute.getStartTime(), updatedRailwayRoute.getStartTime());
        Assertions.assertEquals(railwayRoute.getEndTime(), updatedRailwayRoute.getEndTime());
        Assertions.assertEquals(railwayRoute.getStartStation().getId(), updatedRailwayRoute.getStartStation().getId());
        Assertions.assertEquals(railwayRoute.getFinishStation().getId(), updatedRailwayRoute.getFinishStation().getId());
        Assertions.assertEquals(railwayRoute.getTrain().getId(), updatedRailwayRoute.getTrain().getId());
    }

    @Test
    public void deleteRailwayRoute() {
        RailwayRoute railwayRoute = railwayRouteDao.save(getRailwayRoute(LocalDateTime.now()));
        Assertions.assertNotNull(railwayRoute.getId());
        railwayRouteDao.delete(railwayRoute);
        Assertions.assertThrows(NoSuchElementException.class, () -> railwayRouteDao.findById(railwayRoute.getId()));
    }

    private RailwayRoute getRailwayRoute(LocalDateTime time) {
        return RailwayRoute.builder()
                .startTime(time)
                .endTime(time)
                .startStation(RailwayStation.builder().id(1L).build())
                .finishStation(RailwayStation.builder().id(2L).build())
                .train(Train.builder().id(1L).build())
                .build();
    }
}