package com.kpi.lab2.controllers.console;

import com.kpi.lab2.controllers.console.operations.Operation;
import com.kpi.lab2.models.entities.User;
import com.kpi.lab2.views.InputOutputHelper;
import com.kpi.lab2.views.Range;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ConsoleApplicationController {

    private final InputOutputHelper inputOutputHelper;
    private final List<Operation> operations;
    private User user = null;

    public void run() {
        while (true) {
            List<Operation> applicableOperations = operations.stream().filter(operation -> operation.isApplicable(user)).toList();
            int number = 1;
            for (Operation applicableOperation : applicableOperations) {
                inputOutputHelper.printString(String.format("%d - %s", number++, applicableOperation.getOperationName()));
            }
            int chosenOperation = inputOutputHelper.readNumber("Input your selection:", Range.of(1, applicableOperations.size()));
            user = applicableOperations.get(chosenOperation - 1).doOperation(user);
        }
    }
}
