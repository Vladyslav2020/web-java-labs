package com.kpi.lab2.controllers.web.servlets;

import com.kpi.lab2.controllers.web.servlets.exceptions.InvalidPathVariableException;
import com.kpi.lab2.controllers.web.servlets.exceptions.InvalidRequestParamException;
import com.kpi.lab2.models.entities.RailwayStation;
import com.kpi.lab2.models.services.EntityServicesFactory;
import com.kpi.lab2.models.services.RailwayStationService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "railwayStationServlet", urlPatterns = {"/railway-stations/*"})
public class RailwayStationServlet extends HttpServlet {

    private RailwayStationService railwayStationService;

    @Override
    public void init() throws ServletException {
        super.init();
        railwayStationService = EntityServicesFactory.INSTANCE.getEntityService(RailwayStationService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getPathInfo() != null) {
                Long id = ServletUtils.getPathVariable(request);
                RailwayStation railwayStation = railwayStationService.findById(id);
                request.setAttribute("railwayStation", railwayStation);
                getServletContext().getRequestDispatcher("/manage-railway-station.jsp").forward(request, response);
            } else {
                forwardToGridView(request, response, null);
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
                    deleteRailwayStation(request, response);
                } else {
                    updateRailwayStation(request, response);
                }
            } else {
                createRailwayStation(request, response);
            }
        } catch (InvalidRequestParamException e) {
            String requestDispatcherName = e.getOperationType() == EntityOperationType.SEARCH ? "/railway-stations.jsp" : "/manage-railway-station.jsp";
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(requestDispatcherName);
            ResponseWritingUtils.writeError(request, response, requestDispatcher, e.getMessage());
        } catch (InvalidPathVariableException e) {
            response.sendRedirect("/railway-stations");
        }
    }

    private void deleteRailwayStation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = ServletUtils.getLong(request.getPathInfo().substring("/delete/".length())).orElseThrow(() -> new InvalidPathVariableException("Cannot parse a path variable of type Long."));
        railwayStationService.delete(RailwayStation.builder().id(id).build());
        forwardToGridView(request, response, String.format("A Railway Station (ID=%d) is deleted successfully.", id));
    }

    private void createRailwayStation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RailwayStation railwayStation = getRailwayStation(request, EntityOperationType.CREATE);
        railwayStationService.create(railwayStation);
        forwardToGridView(request, response, "A Railway Station is created successfully.");
    }

    private void updateRailwayStation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = ServletUtils.getPathVariable(request);
        RailwayStation railwayStation = getRailwayStation(request, EntityOperationType.UPDATE);
        railwayStation.setId(id);
        railwayStationService.update(id, railwayStation);
        forwardToGridView(request, response, String.format("A Railway Station (ID=%d) is updated successfully.", id));
    }

    private void forwardToGridView(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException {
        List<RailwayStation> railwayStations = railwayStationService.findAll();
        request.setAttribute("railwayStations", railwayStations);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/railway-stations.jsp");
        if (message != null) {
            ResponseWritingUtils.writeMessage(request, response, requestDispatcher, message);
        }
        requestDispatcher.forward(request, response);
    }

    private RailwayStation getRailwayStation(HttpServletRequest request, EntityOperationType operationType) {
        String name = request.getParameter("name");
        if (name == null || name.isEmpty()) {
            throw new InvalidRequestParamException("Railway Station name cannot be empty.", operationType);
        }
        return RailwayStation.builder()
                .name(name)
                .build();
    }
}
