package com.kpi.lab2;

import com.kpi.lab2.config.JDBCConnectivityManager;
import com.kpi.lab2.controllers.ConsoleApplicationController;
import com.kpi.lab2.controllers.operations.*;
import com.kpi.lab2.daos.*;
import com.kpi.lab2.models.RailwayRoute;
import com.kpi.lab2.models.RailwayStation;
import com.kpi.lab2.models.Ticket;
import com.kpi.lab2.services.*;
import com.kpi.lab2.utils.InputOutputHelper;
import com.kpi.lab2.utils.tables.RailwayRouteTablePrinter;
import com.kpi.lab2.utils.tables.RailwayStationTablePrinter;
import com.kpi.lab2.utils.tables.TicketTablePrinter;

import javax.sql.DataSource;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        JDBCConnectivityManager jdbcConnectivityManager = new JDBCConnectivityManager();
        DataSource dataSource = jdbcConnectivityManager.setUpPool();

        UserDao userDao = new UserDao(dataSource);
        TrainDao trainDao = new TrainDao(dataSource);
        RailwayStationDao railwayStationDao = new RailwayStationDao(dataSource);
        RailwayRouteDao railwayRouteDao = new RailwayRouteDao(dataSource, railwayStationDao, trainDao);
        TicketDao ticketDao = new TicketDao(dataSource, userDao, railwayRouteDao);

        UserService userService = new UserService(userDao);
        RailwayStationService railwayStationService = new RailwayStationService(railwayStationDao);
        RailwayRouteService railwayRouteService = new RailwayRouteService(railwayRouteDao);
        TrainService trainService = new TrainService(trainDao);
        TicketService ticketService = new TicketService(ticketDao);

        InputOutputHelper inputOutputHelper = new InputOutputHelper();
        RailwayStationTablePrinter<RailwayStation> railwayStationTablePrinter = new RailwayStationTablePrinter<>();
        RailwayRouteTablePrinter<RailwayRoute> railwayRouteTablePrinter = new RailwayRouteTablePrinter<>(ticketService);
        TicketTablePrinter<Ticket> ticketTablePrinter = new TicketTablePrinter<>();

        LogInOperation logInOperation = new LogInOperation(inputOutputHelper, userService);
        RailwayStationOperation railwayStationOperation = new RailwayStationOperation(railwayStationService, railwayStationTablePrinter, inputOutputHelper);
        RailwayRouteOperation railwayRouteOperation = new RailwayRouteOperation(railwayRouteService, railwayStationService, trainService, railwayRouteTablePrinter, inputOutputHelper);
        SearchRouteOperation searchRouteOperation = new SearchRouteOperation(railwayStationService, railwayRouteService, ticketService, railwayStationTablePrinter, railwayRouteTablePrinter, ticketTablePrinter, inputOutputHelper);
        LogOutOperation logOutOperation = new LogOutOperation();

        ConsoleApplicationController consoleApplicationController = new ConsoleApplicationController(inputOutputHelper, List.of(logInOperation, railwayStationOperation, railwayRouteOperation, searchRouteOperation, logOutOperation));
        consoleApplicationController.run();
    }
}
