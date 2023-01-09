package com.kpi.lab1.controller;

import com.kpi.lab1.processing.DirectoryProcessor;
import com.kpi.lab1.processing.FileProcessingResult;
import com.kpi.lab1.utils.FileInputOutputHelper;
import com.kpi.lab1.utils.InputOutputHelper;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileProcessingController {
    private ExecutorService executorsPool;
    private DirectoryProcessor directoryProcessor;
    private FileInputOutputHelper fileInputOutputHelper;
    private InputOutputHelper inputOutputHelper;

    public FileProcessingController() {
        this.executorsPool = Executors.newCachedThreadPool();
        this.directoryProcessor = new DirectoryProcessor(executorsPool);
        this.fileInputOutputHelper = new FileInputOutputHelper();
        this.inputOutputHelper = new InputOutputHelper();
    }

    public void run() {
        String directoryName = inputOutputHelper.readFileOrDirectoryPath("Input a directory name: ");
        String resultFileName = inputOutputHelper.readFileOrDirectoryPath("Input result file name: ");
        File directory = new File(directoryName);
        List<FileProcessingResult> fileProcessingResults = directoryProcessor.process(directory);
        inputOutputHelper.printString("Result of processing:");
        fileInputOutputHelper.writeFileProcessingResults(fileProcessingResults, resultFileName);
        fileInputOutputHelper.getAllFileLines(resultFileName).forEach(line -> inputOutputHelper.printString(line));
        executorsPool.shutdown();
    }

}
