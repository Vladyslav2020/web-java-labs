package com.kpi.lab2.models.daos;

import com.kpi.lab2.exceptions.SQLRuntimeException;
import com.kpi.lab2.models.entities.Train;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TrainDao extends EntityDaoBase<Train> {
    private static final Logger logger = Logger.getLogger(TrainDao.class);

    public TrainDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "trains";
    }

    @Override
    protected Train convertToEntity(Map<String, Object> fieldValues) {
        return Train.builder()
                .id((Long) fieldValues.get("id"))
                .number((Long) fieldValues.get("number"))
                .numberOfSeats((Long) fieldValues.get("number_of_seats"))
                .build();
    }

    @Override
    protected Map<String, Object> convertToFieldValueMap(Train entity) {
        Map<String, Object> fieldValues = new HashMap<>();
        if (entity.getId() != null) {
            fieldValues.put("id", entity.getId());
        }
        if (entity.getNumber() != null) {
            fieldValues.put("number", entity.getNumber());
        }
        if (entity.getNumberOfSeats() != null) {
            fieldValues.put("number_of_seats", entity.getNumberOfSeats());
        }
        return fieldValues;
    }

    @Override
    protected Set<String> getColumnsNames() {
        return Set.of("id", "number", "number_of_seats");
    }

    public List<Train> findByNumber(int readNumber) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE number = ?", String.join(", ", getColumnsNames()), getTableName()))
        ) {
            preparedStatement.setInt(1, readNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getEntitiesFromResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("Cannot execute SQL query", e);
            throw new SQLRuntimeException(e);
        }
    }
}
