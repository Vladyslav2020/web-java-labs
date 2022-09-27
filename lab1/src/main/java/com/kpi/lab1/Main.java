package com.kpi.lab1;

import com.kpi.lab1.controller.FileProcessingController;

public class Main {

    public static void main(String[] args) {
        // Variant: 12
        // src/main/resources/data
        // src/main/resources/results.txt
        FileProcessingController fileProcessingController = new FileProcessingController();
        fileProcessingController.run();
    }
}
