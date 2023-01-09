package com.kpi.lab2.models.daos.config;

import javax.sql.DataSource;

public enum DatabaseConnectivityProvider {
    INSTANCE;

    private final DataSource dataSource;

    DatabaseConnectivityProvider() {
        try {
            JDBCConnectivityManager jdbcConnectivityManager = new JDBCConnectivityManager();
            this.dataSource = jdbcConnectivityManager.setUpPool();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
