package com.kpi.lab2.models.daos;

import com.kpi.lab2.models.entities.RailwayStation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

public class RailwayStationDaoTest {
    private final RailwayStationDao railwayStationDao = new RailwayStationDao(EntityDaoTest.getDataSource());

    @Test
    public void readRailwayStations() {
        List<RailwayStation> railwayStations = railwayStationDao.findAll().stream().filter(railwayStation -> railwayStation.getId() <= 5).toList();
        Assertions.assertEquals(5, railwayStations.size());
        for (RailwayStation railwayStation : railwayStations) {
            RailwayStation railwayStationById = railwayStationDao.findById(railwayStation.getId());
            compareRailwayStations(railwayStation, railwayStationById);
        }
    }

    private void compareRailwayStations(RailwayStation expectedRailwayStation, RailwayStation actualRailwayStation) {
        Assertions.assertEquals(expectedRailwayStation.getId(), actualRailwayStation.getId());
        Assertions.assertEquals(expectedRailwayStation.getName(), actualRailwayStation.getName());
    }

    @Test
    public void createRailwayStation() {
        String name = "Test station";
        RailwayStation railwayStation = railwayStationDao.save(getRailwayStation(name));
        Assertions.assertNotNull(railwayStation.getId());
        Assertions.assertEquals(name, railwayStation.getName());
    }

    @Test
    public void updateRailwayStation() {
        RailwayStation railwayStation = getRailwayStation("Test station");
        railwayStation = railwayStationDao.save(railwayStation);
        Assertions.assertNotNull(railwayStation.getId());
        railwayStation.setName("Test station 1");
        RailwayStation updatedRailwayStation = railwayStationDao.save(railwayStation);
        Assertions.assertEquals(railwayStation.getId(), updatedRailwayStation.getId());
        Assertions.assertEquals(railwayStation.getName(), updatedRailwayStation.getName());
    }

    @Test
    public void deleteRailwayStation() {
        RailwayStation railwayStation = railwayStationDao.save(getRailwayStation("Test station"));
        Assertions.assertNotNull(railwayStation.getId());
        railwayStationDao.delete(railwayStation);
        Assertions.assertThrows(NoSuchElementException.class, () -> railwayStationDao.findById(railwayStation.getId()));
    }

    private RailwayStation getRailwayStation(String name) {
        return RailwayStation.builder()
                .name(name)
                .build();
    }
}
