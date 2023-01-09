package com.kpi.lab2.controllers.web.servlets;

import com.kpi.lab2.models.entities.User;
import com.kpi.lab2.models.services.EntityServicesFactory;
import com.kpi.lab2.models.services.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    public static final int SESSION_LIVE_INTERVAL = 30 * 60;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService userService = EntityServicesFactory.INSTANCE.getEntityService(UserService.class);
        List<User> users = userService.findByNameAndPassword(request.getParameter("username"), request.getParameter("password"));
        if (users.isEmpty()) {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            ResponseWritingUtils.writeError(request, response, requestDispatcher, "Username or password is invalid");
            return;
        }
        User authenticatedUser = users.iterator().next();
        openSession(request, response, authenticatedUser);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/user.jsp");
        ResponseWritingUtils.writeMessage(request, response, requestDispatcher, "Login successful.");
        requestDispatcher.forward(request, response);
    }

    private void openSession(HttpServletRequest request, HttpServletResponse response, User authenticatedUser) {
        HttpSession session = request.getSession();
        session.setAttribute("user", authenticatedUser);
        session.setMaxInactiveInterval(SESSION_LIVE_INTERVAL);
        request.setAttribute("user", authenticatedUser);
        Cookie cookie = new Cookie("user", authenticatedUser.getName());
        cookie.setMaxAge(SESSION_LIVE_INTERVAL);
        response.addCookie(cookie);
    }
}
