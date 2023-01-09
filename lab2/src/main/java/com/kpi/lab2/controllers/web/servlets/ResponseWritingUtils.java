package com.kpi.lab2.controllers.web.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseWritingUtils {

    public static void writeError(HttpServletRequest request, HttpServletResponse response, RequestDispatcher requestDispatcher, String errorMessage) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        out.println(String.format("<div class=\"container mt-2\"><div class=\"alert alert-danger\" role=\"alert\">%s</div></div>", errorMessage));
        requestDispatcher.include(request, response);
    }

    public static void writeMessage(HttpServletRequest request, HttpServletResponse response, RequestDispatcher requestDispatcher, String message) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        out.println(String.format("<div class=\"container mt-2\"><div class=\"alert alert-success\" role=\"alert\">%s</div></div>", message));
        requestDispatcher.include(request, response);
    }
}
