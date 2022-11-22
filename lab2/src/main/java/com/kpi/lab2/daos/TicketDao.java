package com.kpi.lab2.daos;

import com.kpi.lab2.exceptions.SQLRuntimeException;
import com.kpi.lab2.models.RailwayRoute;
import com.kpi.lab2.models.Ticket;
import com.kpi.lab2.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TicketDao extends EntityDaoBase<Ticket> {
    private UserDao userDao;
    private RailwayRouteDao railwayRouteDao;

    public TicketDao(DataSource dataSource, UserDao userDao, RailwayRouteDao railwayRouteDao) {
        super(dataSource);
        this.userDao = userDao;
        this.railwayRouteDao = railwayRouteDao;
    }

    @Override
    protected String getTableName() {
        return "tickets";
    }

    @Override
    protected Ticket convertToEntity(Map<String, Object> fieldValues) {
        return Ticket.builder()
                .id((Long) fieldValues.get("id"))
                .route(railwayRouteDao.findById((Long) fieldValues.get("railway_route_id")))
                .seatNumber((Long) fieldValues.get("seat_number"))
                .owner(userDao.findById((Long) fieldValues.get("user_id")))
                .build();
    }

    @Override
    protected Map<String, Object> convertToFieldValueMap(Ticket entity) {
        Map<String, Object> fieldValues = new HashMap<>();
        if (entity.getId() != null) {
            fieldValues.put("id", entity.getId());
        }
        if (entity.getRoute() != null && entity.getRoute().getId() != null) {
            fieldValues.put("railway_route_id", entity.getRoute().getId());
        }
        if (entity.getSeatNumber() != null) {
            fieldValues.put("seat_number", entity.getSeatNumber());
        }
        if (entity.getOwner() != null && entity.getOwner().getId() != null) {
            fieldValues.put("user_id", entity.getOwner().getId());
        }
        return fieldValues;
    }

    @Override
    protected Set<String> getColumnsNames() {
        return Set.of("id", "railway_route_id", "seat_number", "user_id");
    }

    public List<Ticket> findByRoute(RailwayRoute railwayRoute) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE railway_route_id = ? ORDER BY seat_number", String.join(", ", getColumnsNames()), getTableName()))
        ) {
            preparedStatement.setLong(1, railwayRoute.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            return getEntitiesFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public List<Ticket> findByOwner(User owner) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE user_id = ?", String.join(", ", getColumnsNames()), getTableName()))
        ) {
            preparedStatement.setLong(1, owner.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            return getEntitiesFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public <T extends RailwayRoute> Long countByRoute(T obj) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT COUNT(id) AS count FROM %s WHERE railway_route_id = ?", getTableName()))
        ) {
            preparedStatement.setLong(1, obj.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getLong("count");
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }
}
