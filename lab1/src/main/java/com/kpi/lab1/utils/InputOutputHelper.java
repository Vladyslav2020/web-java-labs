package com.kpi.lab1.utils;

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
}
