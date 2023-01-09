package com.kpi.lab2.controllers.console.operations;

import com.kpi.lab2.models.entities.RailwayStation;
import com.kpi.lab2.models.entities.User;
import com.kpi.lab2.models.services.RailwayStationService;
import com.kpi.lab2.views.InputOutputHelper;
import com.kpi.lab2.views.tables.RailwayStationTablePrinter;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RailwayStationOperation implements Operation {

    private RailwayStationService railwayStationService;
    private RailwayStationTablePrinter<RailwayStation> railwayStationTablePrinter;
    private InputOutputHelper inputOutputHelper;

    @Override
    public String getOperationName() {
        return "Operations with Railway Stations";
    }

    @Override
    public boolean isApplicable(User user) {
        return user != null && user.getIsAdmin();
    }

    @Override
    public User doOperation(User user) {
        EntityOperationType operationType = null;
        while (operationType != EntityOperationType.QUIT) {
            operationType = EntityOperationType.readOperationType("Railway Station");
            if (operationType == null) {
                continue;
            }
            switch (operationType) {
                case READ_ALL -> {
                    List<RailwayStation> railwayStations = railwayStationService.findAll();
                    railwayStationTablePrinter.printTable(railwayStations);
                }
                case CREATE -> {
                    RailwayStation railwayStation = RailwayStation.builder()
                            .name(inputOutputHelper.readString("Input new RailwayStation name:"))
                            .build();
                    railwayStationService.create(railwayStation);
                    inputOutputHelper.printString("Railway Station created successfully");
                }
                case UPDATE -> {
                    RailwayStation railwayStation = RailwayStation.builder()
                            .id((long) inputOutputHelper.readNumber("Input ID:"))
                            .name(inputOutputHelper.readString("Input updated name:"))
                            .build();
                    railwayStationService.update(railwayStation.getId(), railwayStation);
                    inputOutputHelper.printString("The Railway Station is successfully updated");
                }
                case DELETE -> {
                    RailwayStation railwayStation = RailwayStation.builder()
                            .id((long) inputOutputHelper.readNumber("Input ID:"))
                            .build();
                    railwayStationService.delete(railwayStation);
                    inputOutputHelper.printString("The Railway Station is successfully deleted");
                }
            }
        }
        return user;
    }
}
