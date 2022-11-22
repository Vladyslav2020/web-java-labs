package com.kpi.lab2.utils;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputOutputHelper {
    private Scanner scanner = new Scanner(System.in);

    public InputOutputHelper() {
        scanner.useDelimiter("\n");
    }

    public String readString(String hint) {
        printString(hint);
        return scanner.next();
    }

    public void printString(String string) {
        System.out.println(string);
    }

    public int readNumber(String message, Range range) {
        int number = 0;
        boolean firstCheck = true;
        while (firstCheck || !range.isInRange(number)) {
            try {
                number = firstCheck ? readNumber(message) : readNumber("Invalid input, please try again: ");
            } catch (InputMismatchException e) {
                scanner.next();
            }
            firstCheck = false;
        }
        return number;
    }

    public int readNumber(String hint) {
        printString(hint);
        while (true) {
            try {
                return readNumber();
            } catch (IllegalArgumentException e) {
                scanner.next();
                printString("Invalid input, please try again: ");
            }
        }
    }

    private int readNumber() {
        return scanner.nextInt();
    }

    public LocalDateTime readDateTime(String hint) {
        printString(hint);
        return LocalDateTime.of(
                readNumber("Input year:"),
                readNumber("Input month:", Range.of(1, 12)),
                readNumber("Input day:", Range.of(1, 31)),
                readNumber("Input hour:", Range.of(0, 23)),
                readNumber("Input minute:", Range.of(0, 59))
        );
    }
}
