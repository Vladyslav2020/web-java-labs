package com.kpi.lab2.controllers.web.servlets;

import com.kpi.lab2.controllers.web.dtos.RailwayRouteDTO;
import com.kpi.lab2.controllers.web.servlets.exceptions.InvalidPathVariableException;
import com.kpi.lab2.controllers.web.servlets.exceptions.InvalidRequestParamException;
import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.RailwayStation;
import com.kpi.lab2.models.entities.Train;
import com.kpi.lab2.models.services.EntityServicesFactory;
import com.kpi.lab2.models.services.RailwayRouteService;
import com.kpi.lab2.models.services.RailwayStationService;
import com.kpi.lab2.models.services.TrainService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "railwayRouteServlet", urlPatterns = {"/railway-routes/*"})
public class RailwayRouteServlet extends HttpServlet {

    private RailwayRouteService railwayRouteService;
    private RailwayStationService railwayStationService;
    private TrainService trainService;

    @Override
    public void init() throws ServletException {
        super.init();
        railwayRouteService = EntityServicesFactory.INSTANCE.getEntityService(RailwayRouteService.class);
        railwayStationService = EntityServicesFactory.INSTANCE.getEntityService(RailwayStationService.class);
        trainService = EntityServicesFactory.INSTANCE.getEntityService(TrainService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getPathInfo() != null) {
                Long id = ServletUtils.getPathVariable(request);
                RailwayRouteDTO railwayRouteDTO = railwayRouteService.findDTOById(id);
                request.setAttribute("railwayRoute", railwayRouteDTO);
                getServletContext().getRequestDispatcher("/manage-railway-route.jsp").forward(request, response);
            } else {
                forwardToGridView(request, response, null, null);
            }
        } catch (InvalidPathVariableException e) {
            response.sendRedirect("/railway-routes");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getPathInfo() != null) {
                if (request.getPathInfo().contains("/delete/")) {
                    deleteRailwayRoute(request, response);
                } else if (request.getPathInfo().contains("/search")) {
                    if (request.getParameter("start-station").isEmpty() && request.getParameter("finish-station").isEmpty()) {
                        forwardToGridView(request, response, null, null);
                    } else {
                        RailwayStation startStation = getRailwayStation(request, "start-station").orElseThrow(() -> new InvalidRequestParamException("The start station is not found.", EntityOperationType.SEARCH));
                        RailwayStation finishStation = getRailwayStation(request, "finish-station").orElseThrow(() -> new InvalidRequestParamException("The finish station is not found.", EntityOperationType.SEARCH));
                        List<RailwayRouteDTO> railwayRoutes = railwayRouteService.findDTOByStartAndFinishStations(startStation, finishStation);
                        forwardToGridView(request, response, railwayRoutes, null);
                    }
                } else {
                    updateRailwayRoute(request, response);
                }
            } else {
                createRailwayRoute(request, response);
            }
        } catch (InvalidRequestParamException e) {
            String requestDispatcherName = e.getOperationType() == EntityOperationType.SEARCH ? "/railway-routes.jsp" : "/manage-railway-route.jsp";
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(requestDispatcherName);
            ResponseWritingUtils.writeError(request, response, requestDispatcher, e.getMessage());
        } catch (InvalidPathVariableException e) {
            response.sendRedirect("/railway-routes");
        }
    }

    private void createRailwayRoute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RailwayRoute railwayRoute = getRailwayRoute(request, EntityOperationType.CREATE);
        railwayRouteService.create(railwayRoute);
        forwardToGridView(request, response, null, "A Railway Route is created successfully.");
    }

    private void updateRailwayRoute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = ServletUtils.getPathVariable(request);
        RailwayRoute railwayRoute = getRailwayRoute(request, EntityOperationType.UPDATE);
        railwayRoute.setId(id);
        railwayRouteService.update(id, railwayRoute);
        forwardToGridView(request, response, null, String.format("A Railway Route (ID=%d) is updated successfully.", id));
    }

    private void deleteRailwayRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = ServletUtils.getLong(request.getPathInfo().substring("/delete/".length())).orElseThrow(() -> new InvalidPathVariableException("Cannot parse a path variable of type Long."));
        railwayRouteService.delete(RailwayRoute.builder().id(id).build());
        forwardToGridView(request, response, null, String.format("A Railway Route (ID=%d) is deleted successfully.", id));
    }

    private void forwardToGridView(HttpServletRequest request, HttpServletResponse response, List<RailwayRouteDTO> railwayRoutes, String message) throws IOException, ServletException {
        if (railwayRoutes == null) {
            railwayRoutes = railwayRouteService.findAllDTO();
        }
        request.setAttribute("railwayRoutes", railwayRoutes);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/railway-routes.jsp");
        if (message != null) {
            ResponseWritingUtils.writeMessage(request, response, requestDispatcher, message);
        }
        requestDispatcher.forward(request, response);
    }

    private RailwayRoute getRailwayRoute(HttpServletRequest request, EntityOperationType operationType) {
        RailwayStation startStation = getRailwayStation(request, "start-station").orElseThrow(() -> new InvalidRequestParamException("The start station is not found.", operationType));
        RailwayStation finishStation = getRailwayStation(request, "finish-station").orElseThrow(() -> new InvalidRequestParamException("The finish station is not found.", operationType));
        Train train = getTrainByNumber(request, "train-number").orElseThrow(() -> new InvalidRequestParamException("The specified train is not found", operationType));
        LocalDateTime startTime = LocalDateTime.parse(request.getParameter("start-time"));
        LocalDateTime endTime = LocalDateTime.parse(request.getParameter("end-time"));
        return RailwayRoute.builder()
                .startStation(startStation)
                .finishStation(finishStation)
                .startTime(startTime)
                .endTime(endTime)
                .train(train)
                .build();
    }

    private Optional<Train> getTrainByNumber(HttpServletRequest request, String paramName) {
        int trainNumber = Integer.parseInt(request.getParameter(paramName));
        List<Train> trains = trainService.findByNumber(trainNumber);
        if (trains.isEmpty()) {
            return Optional.empty();
        }
        Train train = trains.iterator().next();
        return Optional.of(train);
    }

    private Optional<RailwayStation> getRailwayStation(HttpServletRequest request, String paramName) {
        String startStationName = request.getParameter(paramName);
        List<RailwayStation> railwayStations = railwayStationService.findByName(startStationName);
        if (railwayStations.isEmpty()) {
            return Optional.empty();
        }
        RailwayStation startStation = railwayStations.iterator().next();
        return Optional.of(startStation);
    }
}
