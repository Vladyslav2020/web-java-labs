package com.kpi.lab2.controllers.web.servlets;

import com.kpi.lab2.controllers.web.servlets.exceptions.InvalidPathVariableException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class ServletUtils {

    public static Long getPathVariable(HttpServletRequest request) {
        return getLong(request.getPathInfo().substring(1)).orElseThrow(() -> new InvalidPathVariableException("Cannot parse a path variable of type Long."));
    }

    public static Optional<Long> getLong(String str) {
        try {
            return Optional.of(Long.parseLong(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
