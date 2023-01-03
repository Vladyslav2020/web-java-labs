package com.kpi.lab2.controllers.web.servlets.exceptions;

public class InvalidPathVariableException extends RuntimeException {
    public InvalidPathVariableException(String message) {
        super(message);
    }
}
