package com.urise.webapp.model;

import java.io.Serializable;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Organization implements Serializable {
    private final static long serialVersionUID = 1L;

    private final Link homePage;
    private List<Position> positions = new ArrayList<>();

    public Organization(String name, String url, String startDate, String endDate, String title) {
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        Objects.requireNonNull(title, "title must not be null");
        this.homePage = new Link(name, url);
        addPosition(startDate, endDate, title);
    }

    public Organization(String name, String url, String startDate, String endDate, String title, String description) {
        this(name, url, startDate, endDate, title);
        positions.get(positions.size() - 1).setDescription(description);
    }

    public void addPosition(String startDate, String endDate, String title) {
        positions.add(new Position(startDate, endDate, title));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization)) return false;
        Organization that = (Organization) o;
        return homePage.equals(that.homePage) &&
                positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, positions);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homePage=" + homePage +
                ", positions=" + positions +
                '}';
    }

    private static class Position implements Serializable {
        private final static long serialVersionUID = 1L;

        private final YearMonth startDate;
        private final YearMonth endDate;
        private final String title;
        private String description;
        private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

        public Position(String startDate, String endDate, String title) {
            this.startDate = YearMonth.parse(startDate, FORMATTER);
            this.endDate = YearMonth.parse(endDate, FORMATTER);
            this.title = title;
        }

        public Position(String startDate, String endDate, String title, String description) {
            this(startDate, endDate, title);
            this.description = description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "startDate=" + FORMATTER.format(startDate) +
                    ", endDate=" + FORMATTER.format(endDate) +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
