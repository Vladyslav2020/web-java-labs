package com.kpi.lab1.utils;

import com.kpi.lab1.processing.FileProcessingResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileInputOutputHelper {

    public List<String> getAllFileLines(String fileName) {
        try (InputStream inputStream = new FileInputStream(fileName)) {
            List<String> results = new ArrayList<>();
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                results.add(scanner.nextLine());
            }
            return results;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFileProcessingResults(List<FileProcessingResult> fileProcessingResults, String resultsFileName) {
        File file = new File(resultsFileName);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            for (FileProcessingResult fileProcessingResult : fileProcessingResults) {
                outputStream.write((fileProcessingResult.getFileName() + " -> " + fileProcessingResult.getCountFloatNumbers() + "\n").getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
