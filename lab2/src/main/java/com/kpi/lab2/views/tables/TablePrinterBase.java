package com.kpi.lab2.views.tables;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class TablePrinterBase<T> implements TablePrinter<T> {

    public abstract int getIndent();

    @Override
    public void printTable(Collection<T> objects) {
        Map<String, Integer> column2width = getWidthOfColumns(objects);

        String separator = getSeparator(column2width);
        StringBuilder header = getTableHeader(column2width);

        System.out.println(separator);
        System.out.println(header);
        System.out.println(separator);

        objects.forEach(object -> {
            StringBuilder line = new StringBuilder("|");
            getColumnNames().forEach(columnName -> {
                int width = column2width.get(columnName);
                line.append(String.format("%" + width + "s|", getValueInColumn(columnName, object)));
            });
            System.out.println(line);
            System.out.println(separator);
        });
    }

    private StringBuilder getTableHeader(Map<String, Integer> column2width) {
        StringBuilder header = new StringBuilder("|");

        getColumnNames().forEach(columnName -> {
            int width = column2width.get(columnName);
            header.append(String.format("%" + width + "s|", columnName));
        });
        return header;
    }

    private Map<String, Integer> getWidthOfColumns(Collection<T> objects) {
        Map<String, Integer> column2width = getColumnNames().stream().collect(Collectors.toMap(Function.identity(), String::length));
        objects.forEach(object -> getColumnNames().forEach(columnName -> {
            int currentWidth = column2width.get(columnName);
            String valueInColumn = getValueInColumn(columnName, object);
            if (valueInColumn != null) {
                column2width.put(columnName, Math.max(currentWidth, valueInColumn.length()));
            } else {
                column2width.put(columnName, Math.max(currentWidth, 3));
            }
        }));

        column2width.forEach((columnName, width) -> column2width.put(columnName, width + getIndent()));
        return column2width;
    }

    private String getSeparator(Map<String, Integer> column2width) {
        StringBuilder stringBuilder = new StringBuilder("-");
        getColumnNames().forEach(columnName -> {
            int width = column2width.get(columnName);
            stringBuilder.append("-".repeat(width + 1));
        });
        return stringBuilder.toString();
    }
}
