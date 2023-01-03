package com.kpi.lab2.models.services;

import com.kpi.lab2.models.daos.RailwayStationDao;
import com.kpi.lab2.models.entities.RailwayStation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

class RailwayStationServiceTest {
    public static List<RailwayStation> railwayStations = List.of(
            RailwayStation.builder()
                    .id(1L)
                    .name("Railway Station 1")
                    .build(),
            RailwayStation.builder()
                    .id(2L)
                    .name("Railway Station 2")
                    .build(),
            RailwayStation.builder()
                    .id(3L)
                    .name("Railway Station 3")
                    .build(),
            RailwayStation.builder()
                    .id(4L)
                    .name("Railway Station 4")
                    .build()
    );

    private RailwayStationService railwayStationService;

    @BeforeEach
    public void init() {
        RailwayStationDao railwayStationDao = Mockito.mock(RailwayStationDao.class);
        railwayStationService = new RailwayStationService(railwayStationDao);

        Mockito.doReturn(railwayStations).when(railwayStationDao).findAll();

        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0, Long.class);
            return railwayStations.stream().filter(railwayStation -> railwayStation.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
        }).when(railwayStationDao).findById(any(Long.class));

        Mockito.doAnswer(invocation -> {
            String name = invocation.getArgument(0, String.class);
            return railwayStations.stream().filter(railwayStation -> railwayStation.getName().equals(name)).collect(Collectors.toList());
        }).when(railwayStationDao).findByName(any(String.class));
    }

    @Test
    public void findAll() {
        List<RailwayStation> actualRailwayStations = railwayStationService.findAll();
        Assertions.assertEquals(railwayStations.size(), actualRailwayStations.size());
        Assertions.assertEquals(railwayStations, actualRailwayStations);
    }

    @Test
    public void findById() {
        RailwayStation actualRailwayStation = railwayStationService.findById(1L);
        Assertions.assertEquals(railwayStations.get(0), actualRailwayStation);
        Assertions.assertThrows(NoSuchElementException.class, () -> railwayStationService.findById(10L));
    }

    @Test
    public void findByName() {
        List<RailwayStation> actualRailwayStations = railwayStationService.findByName("Railway Station 1");
        Assertions.assertEquals(1, actualRailwayStations.size());
        Assertions.assertEquals(railwayStations.get(0), actualRailwayStations.get(0));
    }
}