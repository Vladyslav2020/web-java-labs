package com.kpi.lab2.models.daos;

import com.kpi.lab2.exceptions.SQLRuntimeException;
import com.kpi.lab2.models.entities.Entity;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@AllArgsConstructor
public abstract class EntityDaoBase<T extends Entity> implements EntityDao<T> {
    private static final Logger logger = Logger.getLogger(EntityDaoBase.class);

    protected DataSource dataSource;

    @Override
    public T findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE id = ?", String.join(", ", getColumnsNames()), getTableName()))
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new NoSuchElementException();
            }
            Map<String, Object> fieldValues = getFieldValuesMap(resultSet);
            return convertToEntity(fieldValues);
        } catch (SQLException e) {
            logger.error("Cannot execute SQL query", e);
            throw new SQLRuntimeException(e);
        }
    }

    @Override
    public List<T> findAll() {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(String.format("SELECT %s FROM %s", String.join(", ", getColumnsNames()), getTableName()))
        ) {
            return getEntitiesFromResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("Cannot execute SQL query", e);
            throw new SQLRuntimeException(e);
        }
    }

    protected List<T> getEntitiesFromResultSet(ResultSet resultSet) throws SQLException {
        List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> fieldValues = getFieldValuesMap(resultSet);
            result.add(convertToEntity(fieldValues));
        }
        return result;
    }

    private Map<String, Object> getFieldValuesMap(ResultSet resultSet) throws SQLException {
        Map<String, Object> fieldValues = new HashMap<>();
        for (String column : getColumnsNames()) {
            if (column.contains("time")) {
                fieldValues.put(column, resultSet.getTimestamp(column).toLocalDateTime());
            } else {
                fieldValues.put(column, resultSet.getObject(column));
            }
        }
        return fieldValues;
    }

    @Override
    public T save(T entity) {
        Map<String, Object> fieldValueMap = convertToFieldValueMap(entity);
        if (entity.getId() == null) {
            String valuesForPreparedStatement = "?, ".repeat(fieldValueMap.size());
            valuesForPreparedStatement = valuesForPreparedStatement.substring(0, valuesForPreparedStatement.length() - 2);
            try (
                    Connection connection = dataSource.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(String.format("INSERT INTO %s(%s) VALUES(%s)", getTableName(), String.join(", ", fieldValueMap.keySet()), valuesForPreparedStatement), Statement.RETURN_GENERATED_KEYS)
            ) {
                int index = 1;
                for (String columnName : fieldValueMap.keySet()) {
                    Object value = fieldValueMap.get(columnName);
                    preparedStatement.setObject(index++, value);
                }
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    logger.warn("Entity save failed. Cannot get a primary key");
                    throw new IllegalStateException("Entity creation failed. No ID obtained");
                }
                long generatedId = generatedKeys.getLong("id");
                entity.setId(generatedId);
                return entity;
            } catch (SQLException e) {
                logger.error("Cannot execute SQL query", e);
                throw new SQLRuntimeException(e);
            }
        } else {
            StringBuilder setStatementBuilder = new StringBuilder();
            fieldValueMap.keySet().stream().filter(key -> !"id".equals(key)).forEach(key -> setStatementBuilder.append(key).append(" = ?,"));
            try (
                    Connection connection = dataSource.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(String.format("UPDATE %s SET %s WHERE id = ?", getTableName(), setStatementBuilder.substring(0, setStatementBuilder.length() - 1)))
            ) {
                int index = 1;
                for (String columnName : fieldValueMap.keySet()) {
                    if ("id".equals(columnName)) {
                        continue;
                    }
                    Object value = fieldValueMap.get(columnName);
                    preparedStatement.setObject(index++, value);
                }
                preparedStatement.setLong(index, entity.getId());
                preparedStatement.executeUpdate();
                return entity;
            } catch (SQLException e) {
                logger.error("Cannot execute SQL query", e);
                throw new SQLRuntimeException(e);
            }
        }
    }

    @Override
    public void delete(T entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Cannot delete an entity. Entity ID cannot be null.");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(String.format("DELETE FROM %s WHERE id = ?", getTableName()))
        ) {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Cannot execute SQL query", e);
            throw new SQLRuntimeException(e);
        }
    }

    protected abstract String getTableName();

    protected abstract T convertToEntity(Map<String, Object> fieldValues);

    protected abstract Map<String, Object> convertToFieldValueMap(T entity);

    protected abstract Set<String> getColumnsNames();
}
