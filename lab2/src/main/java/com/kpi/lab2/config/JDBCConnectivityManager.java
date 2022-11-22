package com.kpi.lab2.config;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;

public class JDBCConnectivityManager {
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String JDBC_DB_URL = "jdbc:postgresql://localhost:5432/java-lab2";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public DataSource setUpPool() throws Exception {
        Class.forName(JDBC_DRIVER);
        GenericObjectPool connectionPool = new GenericObjectPool();
        connectionPool.setMaxActive(5);
        ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, USER, PASSWORD);
        new PoolableConnectionFactory(cf, connectionPool, null, null, false, true);
        return new PoolingDataSource(connectionPool);
    }
}
