package com.kpi.lab2.controllers.web.servlets.exceptions;

import com.kpi.lab2.controllers.web.servlets.EntityOperationType;

public class InvalidRequestParamException extends RuntimeException {
    private final EntityOperationType operationType;

    public InvalidRequestParamException(String message, EntityOperationType operationType) {
        super(message);
        this.operationType = operationType;
    }

    public EntityOperationType getOperationType() {
        return operationType;
    }
}
