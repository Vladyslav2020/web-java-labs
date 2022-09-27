package com.kpi.lab1.utils;

import java.io.File;
import java.util.Scanner;

public class InputOutputHelper {
    private Scanner scanner = new Scanner(System.in);

    public String readString(String hint) {
        printString(hint);
        return scanner.nextLine();
    }

    public void printString(String string) {
        System.out.println(string);
    }

    public String readFileOrDirectoryPath(String message) {
        boolean isPathValid = false;
        String path = null;
        while (!isPathValid) {
            path = readString(message);
            File file = new File(path);
            if (!file.exists()) {
                printString("Invalid path!! Please try again.");
            }
            else {
                isPathValid = true;
            }
        }
        return path;
    }
}
