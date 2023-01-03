package com.kpi.lab2.controllers.console.operations;

import com.kpi.lab2.exceptions.InvalidSeatNumberException;
import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.RailwayStation;
import com.kpi.lab2.models.entities.Ticket;
import com.kpi.lab2.models.entities.User;
import com.kpi.lab2.models.services.RailwayRouteService;
import com.kpi.lab2.models.services.RailwayStationService;
import com.kpi.lab2.models.services.TicketService;
import com.kpi.lab2.views.InputOutputHelper;
import com.kpi.lab2.views.Range;
import com.kpi.lab2.views.tables.RailwayRouteTablePrinter;
import com.kpi.lab2.views.tables.RailwayStationTablePrinter;
import com.kpi.lab2.views.tables.TicketTablePrinter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SearchRouteOperation implements Operation {
    private RailwayStationService railwayStationService;
    private RailwayRouteService railwayRouteService;
    private TicketService ticketService;
    private RailwayStationTablePrinter<RailwayStation> railwayStationTablePrinter;
    private RailwayRouteTablePrinter<RailwayRoute> railwayRouteTablePrinter;
    private TicketTablePrinter<Ticket> ticketTablePrinter;
    private InputOutputHelper inputOutputHelper;

    @Override
    public String getOperationName() {
        return "Search Railway Routes";
    }

    @Override
    public boolean isApplicable(User user) {
        return user != null && !user.getIsAdmin();
    }

    @Override
    public User doOperation(User user) {
        OperationType operationType = null;
        while (operationType != OperationType.QUIT) {
            inputOutputHelper.printString("1 - View the Railway Stations");
            inputOutputHelper.printString("2 - Search the Railway Routes");
            inputOutputHelper.printString("3 - Buy the ticket");
            inputOutputHelper.printString("4 - My tickets");
            inputOutputHelper.printString("5 - Quit");
            int chosenOperation = inputOutputHelper.readNumber("Input your selection:", Range.of(1, OperationType.values().length));
            operationType = OperationType.values()[chosenOperation - 1];
            switch (operationType) {
                case VIEW_STATIONS -> railwayStationTablePrinter.printTable(railwayStationService.findAll());
                case SEARCH_ROUTE -> {
                    String stationName = inputOutputHelper.readString("Input the start Railway Station:");
                    List<RailwayStation> railwayStations = railwayStationService.findByName(stationName);
                    if (railwayStations.isEmpty()) {
                        inputOutputHelper.printString("The station is invalid");
                        break;
                    }
                    RailwayStation startStation = railwayStations.iterator().next();
                    stationName = inputOutputHelper.readString("Input the finish Railway Station:");
                    railwayStations = railwayStationService.findByName(stationName);
                    if (railwayStations.isEmpty()) {
                        inputOutputHelper.printString("The station is invalid");
                        break;
                    }
                    RailwayStation finishStation = railwayStations.iterator().next();
                    List<RailwayRoute> railwayRoutes = railwayRouteService.findByStartAndFinishStations(startStation, finishStation);
                    if (railwayRoutes.isEmpty()) {
                        inputOutputHelper.printString("Not found any route");
                        break;
                    }
                    railwayRouteTablePrinter.printTable(railwayRoutes);
                }
                case BUY_TICKET -> {
                    int routeId = inputOutputHelper.readNumber("Input the route ID:");
                    RailwayRoute railwayRoute;
                    try {
                        railwayRoute = railwayRouteService.findById((long) routeId);
                    } catch (NoSuchElementException e) {
                        inputOutputHelper.printString("The route is invalid");
                        break;
                    }
                    List<Ticket> tickets = ticketService.findByRoute(railwayRoute);
                    Long numberOfSeats = railwayRoute.getTrain().getNumberOfSeats();
                    if (numberOfSeats == tickets.size()) {
                        inputOutputHelper.printString("There are no available tickets");
                        break;
                    }
                    printAvailableTickets(tickets, numberOfSeats);
                    int seatNumber = inputOutputHelper.readNumber("Input the seat number:");
                    try {
                        checkSeatNumber(seatNumber, tickets, numberOfSeats);
                    } catch (InvalidSeatNumberException e) {
                        inputOutputHelper.printString("The seat number is invalid");
                        break;
                    }
                    ticketService.create(Ticket.builder().route(railwayRoute).owner(user).seatNumber((long) seatNumber).build());
                    inputOutputHelper.printString("The ticket is successfully booked");
                }
                case MY_TICKETS -> {
                    List<Ticket> tickets = ticketService.findByOwner(user);
                    if (tickets.isEmpty()) {
                        inputOutputHelper.printString("There is no any ticket");
                        break;
                    }
                    ticketTablePrinter.printTable(tickets);
                }
            }
        }
        return user;
    }

    private void checkSeatNumber(int seatNumber, List<Ticket> tickets, Long numberOfSeats) {
        if (seatNumber < 1 || seatNumber > numberOfSeats) {
            throw new InvalidSeatNumberException();
        }
        for (Ticket ticket : tickets) {
            if (seatNumber == ticket.getSeatNumber()) {
                throw new InvalidSeatNumberException();
            }
        }
    }

    private void printAvailableTickets(List<Ticket> tickets, Long numberOfSeats) {
        long lowerBound = 1;
        StringBuilder stringBuilder = new StringBuilder();
        List<Long> seatNumbers = tickets.stream().map(Ticket::getSeatNumber).collect(Collectors.toList());
        seatNumbers.add(numberOfSeats + 1);
        for (Long seatNumber : seatNumbers) {
            long upperBound = seatNumber - 1;
            if (lowerBound < upperBound) {
                stringBuilder.append(lowerBound).append(" - ").append(upperBound).append(", ");
            }
            if (lowerBound == upperBound) {
                stringBuilder.append(lowerBound).append(", ");
            }
            lowerBound = seatNumber + 1;
        }
        inputOutputHelper.printString("The following tickets are available:");
        inputOutputHelper.printString(stringBuilder.substring(0, stringBuilder.length() - 2));
    }

    private enum OperationType {
        VIEW_STATIONS,
        SEARCH_ROUTE,
        BUY_TICKET,
        MY_TICKETS,
        QUIT
    }
}
