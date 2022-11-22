package com.kpi.lab2.daos;

import com.kpi.lab2.exceptions.SQLRuntimeException;
import com.kpi.lab2.models.RailwayStation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RailwayStationDao extends EntityDaoBase<RailwayStation> {
    public RailwayStationDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "railway_stations";
    }

    @Override
    protected RailwayStation convertToEntity(Map<String, Object> fieldValues) {
        return RailwayStation.builder()
                .id((Long) fieldValues.get("id"))
                .name((String) fieldValues.get("name"))
                .build();
    }

    @Override
    protected Map<String, Object> convertToFieldValueMap(RailwayStation entity) {
        Map<String, Object> fieldValueMap = new HashMap<>();
        if (entity.getId() != null) {
            fieldValueMap.put("id", entity.getId());
        }
        if (entity.getName() != null) {
            fieldValueMap.put("name", entity.getName());
        }
        return fieldValueMap;
    }

    @Override
    protected Set<String> getColumnsNames() {
        return Set.of("id", "name");
    }

    public List<RailwayStation> findByName(String name) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE name = ?", String.join(", ", getColumnsNames()), getTableName()))
        ) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getEntitiesFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }
}
