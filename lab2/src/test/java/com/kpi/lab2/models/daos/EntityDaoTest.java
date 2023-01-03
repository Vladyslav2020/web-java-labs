package com.kpi.lab2.models.daos;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;

public class EntityDaoTest {
    private static final DataSource dataSource = getDataSource();

    public static DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        GenericObjectPool connectionPool = new GenericObjectPool();
        connectionPool.setMaxActive(5);
        ConnectionFactory cf = new DriverManagerConnectionFactory("jdbc:postgresql://localhost:5432/java-lab2-test", "postgres", "postgres");
        new PoolableConnectionFactory(cf, connectionPool, null, null, false, true);
        return new PoolingDataSource(connectionPool);
    }


}
