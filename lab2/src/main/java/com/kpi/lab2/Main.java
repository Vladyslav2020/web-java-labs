package com.kpi.lab2;

import com.kpi.lab2.controllers.console.ConsoleApplicationController;
import com.kpi.lab2.controllers.console.operations.*;
import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.RailwayStation;
import com.kpi.lab2.models.entities.Ticket;
import com.kpi.lab2.models.services.*;
import com.kpi.lab2.views.InputOutputHelper;
import com.kpi.lab2.views.tables.RailwayRouteTablePrinter;
import com.kpi.lab2.views.tables.RailwayStationTablePrinter;
import com.kpi.lab2.views.tables.TicketTablePrinter;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        InputOutputHelper inputOutputHelper = new InputOutputHelper();
        RailwayStationTablePrinter<RailwayStation> railwayStationTablePrinter = new RailwayStationTablePrinter<>();

        TicketService ticketService = EntityServicesFactory.INSTANCE.getEntityService(TicketService.class);
        RailwayStationService railwayStationService = EntityServicesFactory.INSTANCE.getEntityService(RailwayStationService.class);
        RailwayRouteService railwayRouteService = EntityServicesFactory.INSTANCE.getEntityService(RailwayRouteService.class);

        RailwayRouteTablePrinter<RailwayRoute> railwayRouteTablePrinter = new RailwayRouteTablePrinter<>(ticketService);
        TicketTablePrinter<Ticket> ticketTablePrinter = new TicketTablePrinter<>();

        LogInOperation logInOperation = new LogInOperation(inputOutputHelper, EntityServicesFactory.INSTANCE.getEntityService(UserService.class));
        RailwayStationOperation railwayStationOperation = new RailwayStationOperation(railwayStationService, railwayStationTablePrinter, inputOutputHelper);
        RailwayRouteOperation railwayRouteOperation = new RailwayRouteOperation(railwayRouteService, railwayStationService, EntityServicesFactory.INSTANCE.getEntityService(TrainService.class), railwayRouteTablePrinter, inputOutputHelper);
        SearchRouteOperation searchRouteOperation = new SearchRouteOperation(railwayStationService, railwayRouteService, ticketService, railwayStationTablePrinter, railwayRouteTablePrinter, ticketTablePrinter, inputOutputHelper);
        LogOutOperation logOutOperation = new LogOutOperation();

        ConsoleApplicationController consoleApplicationController = new ConsoleApplicationController(inputOutputHelper, List.of(logInOperation, railwayStationOperation, railwayRouteOperation, searchRouteOperation, logOutOperation));
        consoleApplicationController.run();
    }
}
