package com.kpi.lab2.views.tables;

import com.kpi.lab2.models.RailwayRoute;
import com.kpi.lab2.models.services.TicketService;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class RailwayRouteTablePrinter<T extends RailwayRoute> extends TablePrinterBase<T> {
    private static final List<String> columns = Stream.of(Columns.values()).map(Columns::getName).collect(Collectors.toList());

    private TicketService ticketService;

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
                case START_STATION -> obj.getStartStation().getName();
                case START_TIME -> obj.getStartTime().toString();
                case FINISH_STATION -> obj.getFinishStation().getName();
                case END_TIME -> obj.getEndTime().toString();
                case DURATION -> convertToString(Duration.between(obj.getStartTime(), obj.getEndTime()));
                case TICKETS_NUMBER ->
                        String.valueOf(obj.getTrain().getNumberOfSeats() - ticketService.countByRoute(obj));
                case TRAIN -> String.valueOf(obj.getTrain().getNumber());
            };
        }
        throw new IllegalArgumentException("Column name is invalid");
    }

    private String convertToString(Duration duration) {
        return duration.getSeconds() / 3600 + "h " + duration.getSeconds() % 3600 / 60 + "m";
    }

    @Override
    public int getIndent() {
        return 2;
    }

    public enum Columns {
        ID("ID"),
        START_STATION("Start Station"),
        START_TIME("Start time"),
        FINISH_STATION("Finish Station"),
        END_TIME("End Time"),
        DURATION("Duration"),
        TICKETS_NUMBER("Available tickets"),
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
