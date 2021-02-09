package com.urise.webapp.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Organization {
    private String name;
    private String position;
    private String activity;
    private Calendar start;
    private Calendar end;
    private SimpleDateFormat ft =
            new SimpleDateFormat("yyyy.MM");

    public Organization(String name, String position, String activity, Calendar start, Calendar end) {
        this(name, activity, start, end);
        this.position = position;
    }

    public Organization(String name, String activity, Calendar start, Calendar end) {
        this.name = name;
        this.activity = activity;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        position = (position == null ? "" : (position + "\n"));
        return name + "\n" + position + ft.format(start.getTime()) + " - " + ft.format(end.getTime()) + " | " + activity;
    }
}
