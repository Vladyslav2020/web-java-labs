package com.kpi.lab2.controllers.operations;

import com.kpi.lab2.models.RailwayRoute;
import com.kpi.lab2.models.RailwayStation;
import com.kpi.lab2.models.Train;
import com.kpi.lab2.models.User;
import com.kpi.lab2.services.RailwayRouteService;
import com.kpi.lab2.services.RailwayStationService;
import com.kpi.lab2.services.TrainService;
import com.kpi.lab2.utils.InputOutputHelper;
import com.kpi.lab2.utils.tables.RailwayRouteTablePrinter;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RailwayRouteOperation implements Operation {

    private RailwayRouteService railwayRouteService;
    private RailwayStationService railwayStationService;
    private TrainService trainService;
    private RailwayRouteTablePrinter<RailwayRoute> railwayRouteTablePrinter;
    private InputOutputHelper inputOutputHelper;

    @Override
    public String getOperationName() {
        return "Operation with Railway Route";
    }

    @Override
    public boolean isApplicable(User user) {
        return user != null && user.getIsAdmin();
    }

    @Override
    public User doOperation(User user) {
        EntityOperationType operationType = null;
        while (operationType != EntityOperationType.QUIT) {
            operationType = EntityOperationType.readOperationType("Railway Route");
            if (operationType == null) {
                continue;
            }
            switch (operationType) {
                case READ_ALL -> {
                    List<RailwayRoute> railwayRoutes = railwayRouteService.findAll();
                    railwayRouteTablePrinter.printTable(railwayRoutes);
                }
                case CREATE -> {
                    List<RailwayStation> railwayStations = railwayStationService.findByName(inputOutputHelper.readString("Input the start station name:"));
                    if (railwayStations.isEmpty()) {
                        inputOutputHelper.printString("The start station is invalid");
                        break;
                    }
                    RailwayStation startStation = railwayStations.iterator().next();
                    railwayStations = railwayStationService.findByName(inputOutputHelper.readString("Input the finish station name:"));
                    if (railwayStations.isEmpty()) {
                        inputOutputHelper.printString("The finish station is invalid");
                        break;
                    }
                    RailwayStation finishStation = railwayStations.iterator().next();
                    List<Train> trains = trainService.findByNumber(inputOutputHelper.readNumber("Input train number:"));
                    if (trains.isEmpty()) {
                        inputOutputHelper.printString("The train is invalid");
                        break;
                    }
                    Train train = trains.iterator().next();
                    RailwayRoute railwayRoute = RailwayRoute.builder()
                            .startStation(startStation)
                            .finishStation(finishStation)
                            .startTime(inputOutputHelper.readDateTime("Input start time:"))
                            .endTime(inputOutputHelper.readDateTime("Input end time:"))
                            .train(train)
                            .build();
                    railwayRouteService.create(railwayRoute);
                    inputOutputHelper.printString("Railway Route created successfully");
                }
                case UPDATE -> {
                    long id = inputOutputHelper.readNumber("Input ID:");
                    List<RailwayStation> railwayStations = railwayStationService.findByName(inputOutputHelper.readString("Input the updated start station name:"));
                    if (railwayStations.isEmpty()) {
                        inputOutputHelper.printString("The start station is invalid");
                        break;
                    }
                    RailwayStation startStation = railwayStations.iterator().next();
                    railwayStations = railwayStationService.findByName(inputOutputHelper.readString("Input the updated finish station name:"));
                    if (railwayStations.isEmpty()) {
                        inputOutputHelper.printString("The finish station is invalid");
                        break;
                    }
                    RailwayStation finishStation = railwayStations.iterator().next();
                    List<Train> trains = trainService.findByNumber(inputOutputHelper.readNumber("Input updated train number:"));
                    if (trains.isEmpty()) {
                        inputOutputHelper.printString("The train is invalid");
                        break;
                    }
                    Train train = trains.iterator().next();
                    RailwayRoute railwayRoute = RailwayRoute.builder()
                            .id(id)
                            .startStation(startStation)
                            .finishStation(finishStation)
                            .startTime(inputOutputHelper.readDateTime("Input the updated start time:"))
                            .endTime(inputOutputHelper.readDateTime("Input the updated end time:"))
                            .train(train)
                            .build();
                    railwayRouteService.update(railwayRoute.getId(), railwayRoute);
                    inputOutputHelper.printString("The Railway Route is successfully updated");
                }
                case DELETE -> {
                    RailwayRoute railwayRoute = RailwayRoute.builder()
                            .id((long) inputOutputHelper.readNumber("Input ID:"))
                            .build();
                    railwayRouteService.delete(railwayRoute);
                    inputOutputHelper.printString("The Railway Route is successfully deleted");
                }
            }
        }
        return user;
    }
}
