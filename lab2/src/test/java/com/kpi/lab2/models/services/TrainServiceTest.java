package com.kpi.lab2.models.services;

import com.kpi.lab2.models.daos.TrainDao;
import com.kpi.lab2.models.entities.Train;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

class TrainServiceTest {
    public static List<Train> trains = List.of(
            Train.builder()
                    .id(1L)
                    .number(10L)
                    .numberOfSeats(50L)
                    .build(),
            Train.builder()
                    .id(2L)
                    .number(20L)
                    .numberOfSeats(100L)
                    .build(),
            Train.builder()
                    .id(3L)
                    .number(30L)
                    .numberOfSeats(80L)
                    .build()
    );

    private TrainService trainService;

    @BeforeEach
    public void init() {
        TrainDao trainDao = Mockito.mock(TrainDao.class);
        trainService = new TrainService(trainDao);

        Mockito.doReturn(trains).when(trainDao).findAll();

        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0, Long.class);
            return trains.stream().filter(train -> train.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
        }).when(trainDao).findById(any(Long.class));

        Mockito.doAnswer(invocation -> {
            long number = invocation.getArgument(0, Integer.class);
            return trains.stream().filter(train -> train.getNumber().equals(number)).collect(Collectors.toList());
        }).when(trainDao).findByNumber(any(Integer.class));
    }

    @Test
    public void findAll() {
        List<Train> actualTrains = trainService.findAll();
        Assertions.assertEquals(trains.size(), actualTrains.size());
        Assertions.assertEquals(trains, actualTrains);
    }

    @Test
    public void findById() {
        Train actualTrains = trainService.findById(1L);
        Assertions.assertEquals(trains.get(0), actualTrains);
        Assertions.assertThrows(NoSuchElementException.class, () -> trainService.findById(10L));
    }

    @Test
    void findByNumber() {
        List<Train> actualTrains = trainService.findByNumber(10);
        Assertions.assertEquals(1, actualTrains.size());
        Assertions.assertEquals(trains.get(0), actualTrains.get(0));
    }
}