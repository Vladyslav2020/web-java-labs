package com.kpi.lab2.utils.tables;

import java.util.Collection;

public interface TablePrinter<T> {

    Collection<String> getColumnNames();

    String getValueInColumn(String columnName, T obj);

    void printTable(Collection<T> objects);
}
