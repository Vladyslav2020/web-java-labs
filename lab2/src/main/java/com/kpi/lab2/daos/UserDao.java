package com.kpi.lab2.daos;

import com.kpi.lab2.exceptions.SQLRuntimeException;
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

public class UserDao extends EntityDaoBase<User> {
    public UserDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    protected User convertToEntity(Map<String, Object> fieldValues) {
        return User.builder()
                .id((Long) fieldValues.get("id"))
                .name((String) fieldValues.get("name"))
                .password((String) fieldValues.get("password"))
                .isAdmin((Boolean) fieldValues.get("is_admin"))
                .build();
    }

    @Override
    protected Map<String, Object> convertToFieldValueMap(User entity) {
        Map<String, Object> fieldValues = new HashMap<>();
        if (entity.getId() != null) {
            fieldValues.put("id", entity.getId());
        }
        if (entity.getName() != null) {
            fieldValues.put("name", entity.getName());
        }
        if (entity.getPassword() != null) {
            fieldValues.put("password", entity.getPassword());
        }
        if (entity.getIsAdmin() != null) {
            fieldValues.put("is_admin", entity.getIsAdmin());
        }
        return fieldValues;
    }

    @Override
    protected Set<String> getColumnsNames() {
        return Set.of("id", "name", "password", "is_admin");
    }

    public List<User> findByNameAndPassword(String name, String password) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE name = ? AND password = ?", String.join(", ", getColumnsNames()), getTableName()))
        ) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getEntitiesFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }
}
