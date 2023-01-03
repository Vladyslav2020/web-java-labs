package com.kpi.lab2.controllers.web.filters;

import com.kpi.lab2.models.entities.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession(false);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            request.setAttribute("user", user);
            validateRequest(user, httpServletRequest, httpServletResponse);
            chain.doFilter(request, response);
        } else {
            RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher("index.jsp");
            requestDispatcher.forward(request, response);
            chain.doFilter(request, response);
        }
    }

    private void validateRequest(User user, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher("error.jsp");
        if (user == null) {
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
        }
        if (user.getIsAdmin() == null) {
            user.setIsAdmin(false);
        }
        if (user.getIsAdmin()) {
            return;
        }
        if (!httpServletRequest.getMethod().equals("GET") && (httpServletRequest.getServletPath().contains("station") ||
                httpServletRequest.getServletPath().contains("route") || httpServletRequest.getServletPath().contains("train")
                || httpServletRequest.getServletPath().contains("user")) && !httpServletRequest.getRequestURI().contains("/search")) {
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
        }
    }
}
