package com.kpi.lab2.views.tables;

import com.kpi.lab2.models.entities.Ticket;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TicketTablePrinter<T extends Ticket> extends TablePrinterBase<T> {

    private static final List<String> columns = Stream.of(Columns.values()).map(Columns::getName).collect(Collectors.toList());

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
                case ROUTE_ID -> String.valueOf(obj.getRoute().getId());
                case SEAT_NUMBER -> String.valueOf(obj.getSeatNumber());
                case START_TIME -> obj.getRoute().getStartTime().toString();
                case END_TIME -> obj.getRoute().getEndTime().toString();
                case TRAIN -> String.valueOf(obj.getRoute().getTrain().getNumber());
            };
        }
        throw new IllegalArgumentException("Column name is invalid");
    }

    @Override
    public int getIndent() {
        return 2;
    }

    public enum Columns {
        ID("ID"),
        ROUTE_ID("Route ID"),
        SEAT_NUMBER("Seat Number"),
        START_TIME("Start time"),
        END_TIME("End Time"),
        TRAIN("Train");

        private final String name;

        Columns(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
