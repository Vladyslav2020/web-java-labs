package com.kpi.lab1.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class FileProcessor implements Callable<FileProcessingResult> {
    private File file;

    public FileProcessor(File file) {
        this.file = file;
    }

    @Override
    public FileProcessingResult call() throws Exception {
        try (InputStream inputStream = new FileInputStream(file)) {
            Scanner scanner = new Scanner(inputStream);
            int countFloatNumbers = 0;
            while (scanner.hasNext()) {
                try {
                    scanner.nextFloat();
                    countFloatNumbers++;
                } catch (NumberFormatException | InputMismatchException e) {
                    scanner.next();
                }
            }
            return new FileProcessingResult(file.getName(), countFloatNumbers);
        }
    }
}
