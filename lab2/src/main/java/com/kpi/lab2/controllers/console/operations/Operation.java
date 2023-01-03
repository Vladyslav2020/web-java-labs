package com.kpi.lab2.controllers.console.operations;

import com.kpi.lab2.models.entities.User;

public interface Operation {

    String getOperationName();

    boolean isApplicable(User user);

    User doOperation(User user);
}
