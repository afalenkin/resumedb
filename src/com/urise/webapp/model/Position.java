package com.urise.webapp.model;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Position {
    private String position;
    private String activity;
    private YearMonth start;
    private YearMonth end;
    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("MM/yyyy");


    public Position(String position, String activity, String startDate, String endDate) {
        this(activity, startDate, endDate);
        this.position = position;
    }

    public Position(String activity, String startDate, String endDate) {
        this.activity = activity;
        this.start = YearMonth.parse(startDate, pattern);
        this.end = YearMonth.parse(endDate, pattern);
    }

    @Override
    public String toString() {
        position = (position == null ? "" : (position + "\n"));
        return position + pattern.format(start) + " - " + pattern.format(end) + " | " + activity;
    }
}
