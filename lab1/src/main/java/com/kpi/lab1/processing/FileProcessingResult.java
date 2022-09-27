package com.kpi.lab1.processing;

public class FileProcessingResult {
    private String fileName;
    private int countFloatNumbers;

    public FileProcessingResult(String fileName, int countFloatNumbers) {
        this.fileName = fileName;
        this.countFloatNumbers = countFloatNumbers;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCountFloatNumbers() {
        return countFloatNumbers;
    }

    public void setCountFloatNumbers(int countFloatNumbers) {
        this.countFloatNumbers = countFloatNumbers;
    }
}
