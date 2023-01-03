package com.kpi.lab2.models.daos.config;

import org.apache.log4j.Logger;

import javax.sql.DataSource;

public enum DatabaseConnectivityProvider {
    INSTANCE;
    private final Logger logger = Logger.getLogger(DatabaseConnectivityProvider.class);

    private final DataSource dataSource;

    DatabaseConnectivityProvider() {
        try {
            JDBCConnectivityManager jdbcConnectivityManager = new JDBCConnectivityManager();
            this.dataSource = jdbcConnectivityManager.setUpPool();
        } catch (Exception e) {
            logger.error("Cannot connect to the database", e);
            throw new RuntimeException(e);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
