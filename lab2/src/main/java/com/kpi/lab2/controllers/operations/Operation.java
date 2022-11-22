package com.kpi.lab2.controllers.operations;

import com.kpi.lab2.models.User;

public interface Operation {

    String getOperationName();

    boolean isApplicable(User user);

    User doOperation(User user);
}
