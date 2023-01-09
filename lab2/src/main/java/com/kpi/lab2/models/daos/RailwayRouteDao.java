package com.kpi.lab2.models.daos;

import com.kpi.lab2.exceptions.SQLRuntimeException;
import com.kpi.lab2.models.entities.RailwayRoute;
import com.kpi.lab2.models.entities.RailwayStation;
import com.kpi.lab2.models.entities.Train;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RailwayRouteDao extends EntityDaoBase<RailwayRoute> {
    private static final Logger logger = Logger.getLogger(RailwayRouteDao.class);

    public RailwayRouteDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "railway_routes";
    }

    @Override
    protected RailwayRoute convertToEntity(Map<String, Object> fieldValues) {
        return RailwayRoute.builder()
                .id((Long) fieldValues.get("id"))
                .startTime((LocalDateTime) fieldValues.get("start_time"))
                .endTime((LocalDateTime) fieldValues.get("end_time"))
                .startStation(RailwayStation.builder().id((Long) fieldValues.get("start_station_id")).build())
                .finishStation(RailwayStation.builder().id((Long) fieldValues.get("finish_station_id")).build())
                .train(Train.builder().id((Long) fieldValues.get("train_id")).build())
                .build();
    }

    @Override
    protected Map<String, Object> convertToFieldValueMap(RailwayRoute entity) {
        Map<String, Object> fieldValues = new HashMap<>();
        if (entity.getId() != null) {
            fieldValues.put("id", entity.getId());
        }
        if (entity.getStartTime() != null) {
            fieldValues.put("start_time", entity.getStartTime());
        }
        if (entity.getEndTime() != null) {
            fieldValues.put("end_time", entity.getEndTime());
        }
        if (entity.getStartStation() != null && entity.getStartStation().getId() != null) {
            fieldValues.put("start_station_id", entity.getStartStation().getId());
        }
        if (entity.getFinishStation() != null && entity.getFinishStation().getId() != null) {
            fieldValues.put("finish_station_id", entity.getFinishStation().getId());
        }
        if (entity.getTrain() != null && entity.getTrain().getId() != null) {
            fieldValues.put("train_id", entity.getTrain().getId());
        }
        return fieldValues;
    }

    @Override
    protected Set<String> getColumnsNames() {
        return Set.of("id", "start_time", "end_time", "start_station_id", "finish_station_id", "train_id");
    }

    public List<RailwayRoute> findByStartStationAndFinishStation(RailwayStation startStation, RailwayStation finishStation) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE start_station_id = ? AND finish_station_id = ?", String.join(", ", getColumnsNames()), getTableName()))
        ) {
            preparedStatement.setLong(1, startStation.getId());
            preparedStatement.setLong(2, finishStation.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            return getEntitiesFromResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("Cannot execute SQL query", e);
            throw new SQLRuntimeException(e);
        }
    }
}
