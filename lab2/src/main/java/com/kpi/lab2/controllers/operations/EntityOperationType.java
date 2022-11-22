package com.kpi.lab2.controllers.operations;

import com.kpi.lab2.utils.InputOutputHelper;
import com.kpi.lab2.utils.Range;

public enum EntityOperationType {
    READ_ALL,
    CREATE,
    UPDATE,
    DELETE,
    QUIT;

    private static InputOutputHelper inputOutputHelper = new InputOutputHelper();

    public static EntityOperationType readOperationType(String entityName) {
        inputOutputHelper.printString("1 - Read " + entityName);
        inputOutputHelper.printString("2 - Create " + entityName);
        inputOutputHelper.printString("3 - Update " + entityName);
        inputOutputHelper.printString("4 - Delete " + entityName);
        inputOutputHelper.printString("5 - Quit " + entityName);
        int choice = inputOutputHelper.readNumber("Input your selection:", Range.of(1, EntityOperationType.values().length));
        return EntityOperationType.values()[choice - 1];
    }
}
