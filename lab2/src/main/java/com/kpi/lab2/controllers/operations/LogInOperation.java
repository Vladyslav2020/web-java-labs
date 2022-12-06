package com.kpi.lab2.controllers.operations;

import com.kpi.lab2.models.User;
import com.kpi.lab2.models.services.UserService;
import com.kpi.lab2.views.InputOutputHelper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class LogInOperation implements Operation {

    private InputOutputHelper inputOutputHelper;
    private UserService userService;

    @Override
    public String getOperationName() {
        return "Log In";
    }

    @Override
    public boolean isApplicable(User user) {
        return user == null;
    }

    @Override
    public User doOperation(User user) {
        String name = inputOutputHelper.readString("Input name:");
        String password = inputOutputHelper.readString("Input password:");
        List<User> users = userService.findByNameAndPassword(name, password);
        if (users.isEmpty()) {
            inputOutputHelper.printString("Invalid credentials");
            return null;
        }
        inputOutputHelper.printString("Successfully logged in");
        return users.iterator().next();
    }
}
