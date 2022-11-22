package com.kpi.lab2.utils.tables;

import com.kpi.lab2.models.RailwayStation;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RailwayStationTablePrinter<T extends RailwayStation> extends TablePrinterBase<T> {

    private static List<String> columns = Stream.of(Columns.values()).map(Columns::getName).collect(Collectors.toList());

    @Override
    public Collection<String> getColumnNames() {
        return columns;
    }

    @Override
    public String getValueInColumn(String columnName, T obj) {
        Optional<Columns> columnOptional = Stream.of(Columns.values()).filter(column -> column.getName().equals(columnName)).findFirst();
        if (columnOptional.isPresent()) {
            return switch (columnOptional.get()) {
                case ID -> String.valueOf(obj.getId());
                case NAME -> obj.getName();
            };
        }
        throw new IllegalArgumentException("Column name is invalid");
    }

    @Override
    public int getIndent() {
        return 2;
    }

    private enum Columns {
        ID("ID"),
        NAME("Name");

        private String name;

        Columns(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
