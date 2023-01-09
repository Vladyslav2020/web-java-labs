package com.kpi.lab2.controllers.console.operations;

import com.kpi.lab2.models.entities.User;

public class LogOutOperation implements Operation {
    @Override
    public String getOperationName() {
        return "Log out";
    }

    @Override
    public boolean isApplicable(User user) {
        return user != null;
    }

    @Override
    public User doOperation(User user) {
        return null;
    }
}
