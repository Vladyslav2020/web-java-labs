package com.kpi.lab1.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DirectoryProcessor {
    private ExecutorService executorsPool;

    public DirectoryProcessor(ExecutorService executorsPool) {
        this.executorsPool = executorsPool;
    }

    public List<FileProcessingResult> process(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        List<FileProcessingResult> results = new ArrayList<>();
        List<Future<FileProcessingResult>> futureResults = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                results.addAll(process(file));
            } else if (file.getName().endsWith(".txt")) {
                futureResults.add(executorsPool.submit(new FileProcessor(file)));
            }
        }
        results.addAll(futureResults.stream().map(result -> {
            try {
                return result.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList());
        return results;
    }
}
