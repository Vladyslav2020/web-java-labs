package com.kpi.lab2.controllers.web.servlets;

import com.kpi.lab2.controllers.web.dtos.RailwayRouteDTO;
import com.kpi.lab2.controllers.web.servlets.exceptions.InvalidPathVariableException;
import com.kpi.lab2.controllers.web.servlets.exceptions.InvalidRequestParamException;
import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.Ticket;
import com.kpi.lab2.models.entities.User;
import com.kpi.lab2.models.services.EntityServicesFactory;
import com.kpi.lab2.models.services.RailwayRouteService;
import com.kpi.lab2.models.services.TicketService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@WebServlet(name = "ticketServlet", urlPatterns = {"/tickets/*"})
public class TicketServlet extends HttpServlet {

    private TicketService ticketService;
    private RailwayRouteService railwayRouteService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.ticketService = EntityServicesFactory.INSTANCE.getEntityService(TicketService.class);
        this.railwayRouteService = EntityServicesFactory.INSTANCE.getEntityService(RailwayRouteService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String parameter = request.getParameter("railway-route-id");
            if (parameter != null && !parameter.isEmpty()) {
                Long railwayRouteId = ServletUtils.getLong(parameter).orElseThrow(() -> new InvalidRequestParamException("The ID of Railway Route is invalid.", EntityOperationType.SEARCH));
                RailwayRouteDTO railwayRouteDTO = railwayRouteService.findDTOById(railwayRouteId);
                List<Ticket> purchasedTickets = ticketService.findByRoute(RailwayRoute.builder().id(railwayRouteId).build());
                request.setAttribute("railwayRouteId", railwayRouteId);
                request.setAttribute("availableSeats", getAvailableTickets(purchasedTickets, railwayRouteDTO.getTrain().getNumberOfSeats()));
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/buy-ticket.jsp");
                requestDispatcher.forward(request, response);
            } else {
                forwardToGridView(request, response, null);
            }
        } catch (InvalidRequestParamException e) {
            finishWithError(request, response, e.getMessage(), "/buy-ticket.jsp");
        } catch (NoSuchElementException e) {
            finishWithError(request, response, "The Railway Route is not found.", "/buy-ticket.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getPathInfo() != null) {
                if (request.getPathInfo().contains("/delete/")) {
                    deleteTickets(request, response);
                }
            } else {
                createTicket(request, response);
            }
        } catch (NoSuchElementException e) {
            finishWithError(request, response, "The Railway Route is not found.", "/buy-ticket.jsp");
        } catch (InvalidRequestParamException e) {
            finishWithError(request, response, e.getMessage(), "/buy-ticket.jsp");
        }
    }

    private void createTicket(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Ticket ticket = getTicket(request);
        ticketService.create(ticket);
        forwardToGridView(request, response, "The ticket is successfully acquired.");
    }

    private Ticket getTicket(HttpServletRequest request) {
        Long railwayRouteId = Long.parseLong(request.getParameter("railway-route-id"));
        RailwayRouteDTO railwayRouteDTO = railwayRouteService.findDTOById(railwayRouteId);
        Long seatNumber = Long.parseLong(request.getParameter("seat-number"));
        List<Ticket> tickets = ticketService.findByRoute(RailwayRoute.builder().id(railwayRouteId).build());
        Long numberOfSeats = railwayRouteDTO.getTrain().getNumberOfSeats();
        if (seatNumber < 0 || seatNumber > numberOfSeats) {
            throw new InvalidRequestParamException("The seat number is invalid.", EntityOperationType.CREATE);
        }
        for (Ticket ticket : tickets) {
            if (ticket.getSeatNumber().equals(seatNumber)) {
                throw new InvalidRequestParamException("The seat number is already taken.", EntityOperationType.CREATE);
            }
        }
        return Ticket.builder()
                .seatNumber(seatNumber)
                .route(RailwayRoute.builder().id(railwayRouteId).build())
                .owner(getUserFromSession(request))
                .build();
    }

    private void deleteTickets(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = ServletUtils.getLong(request.getPathInfo().substring("/delete/".length())).orElseThrow(() -> new InvalidPathVariableException("Cannot parse a path variable of type Long."));
        ticketService.delete(Ticket.builder().id(id).build());
        forwardToGridView(request, response, String.format("A Ticket (ID=%d) is deleted successfully.", id));
    }

    private String getAvailableTickets(List<Ticket> tickets, Long numberOfSeats) {
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
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }

    private void finishWithError(HttpServletRequest request, HttpServletResponse response, String message, String requestDispatcherName) throws IOException, ServletException {
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(requestDispatcherName);
        ResponseWritingUtils.writeError(request, response, requestDispatcher, message);
    }

    private void forwardToGridView(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException {
        User user = getUserFromSession(request);
        List<Ticket> tickets = ticketService.findByOwner(user);
        request.setAttribute("tickets", tickets);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/my-tickets.jsp");
        if (message != null) {
            ResponseWritingUtils.writeMessage(request, response, requestDispatcher, message);
        }
        requestDispatcher.forward(request, response);
    }

    private User getUserFromSession(HttpServletRequest request) {
        return (User) request.getSession(false).getAttribute("user");
    }
}
