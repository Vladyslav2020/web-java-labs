package com.kpi.lab2.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Range {
    private int lowerBound;
    private int upperBound;

    public boolean isInRange(long number) {
        return number >= lowerBound && number <= upperBound;
    }

    public static Range of(int lowerBound, int upperBound) {
        return new Range(lowerBound, upperBound);
    }
}
