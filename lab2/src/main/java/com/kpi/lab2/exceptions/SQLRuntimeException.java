package com.kpi.lab2.exceptions;

import java.sql.SQLException;

public class SQLRuntimeException extends RuntimeException {
    public SQLRuntimeException(SQLException e) {
        super(e);
    }
}
