package com.kpi.lab1.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class DirectoryProcessor {
    private ExecutorService executorsPool;

    public DirectoryProcessor(ExecutorService executorsPool) {
        this.executorsPool = executorsPool;
    }

    public List<FileProcessingResult> process(File file) {
        File[] files = file.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        List<FileProcessor> fileProcessingTasks = Arrays.stream(files).filter(file1 -> file1.getName().endsWith(".txt")).map(FileProcessor::new).toList();
        List<Future<FileProcessingResult>> futureResults = fileProcessingTasks.stream().map(fileProcessor -> executorsPool.submit(fileProcessor)).toList();
        return futureResults.stream().map(result -> {
            try {
                return result.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
