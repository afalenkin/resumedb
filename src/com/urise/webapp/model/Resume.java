package com.urise.webapp.model;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {
    // Unique identifier
    private final String uuid;
    private String fullName;
    public EnumMap<ContactType, String> contacts = new EnumMap<ContactType, String>(ContactType.class);
    public EnumMap<SectionType, Content> sections = new EnumMap<SectionType, Content>(SectionType.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid can not be empty");
        Objects.requireNonNull(fullName, "full name can not be empty");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public void printResume() {
        System.out.println(fullName);
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()
        ) {
            System.out.println(entry.getKey().name() + " : " + entry.getValue());
        }

        System.out.println();

        for (Map.Entry<SectionType, Content> entry : sections.entrySet()
        ) {
            System.out.println(entry.getKey().name());
            entry.getValue().printContent();
        }
    }

    public String getFullName() {
        return fullName;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return ("Resume № " + uuid + "| Full name: " + fullName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resume)) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) &&
                fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName);
    }


    @Override
    public int compareTo(Resume o) {
        int compare = fullName.compareTo(o.getFullName());
        return compare != 0 ? compare : uuid.compareTo(o.getUuid());
    }

    // inner util classes

    public abstract static class Content<C> {
        protected String text;
        protected List<C> points;

        public void printContent() {
            if (text != null && !text.isEmpty()) {
                System.out.println(text);
            }
            if (points != null && !points.isEmpty()) {
                for (C point : points
                ) {
                    System.out.println(point);
                }
            }
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            localSetText(text);
        }

        public List<C> getPoints() {
            return points;
        }

        public void setPoints(List<C> points) {
            localSetPoints(points);
        }

        protected abstract void localSetPoints(List<C> points);

        protected abstract void localSetText(String text);
    }

    public static class TextContent extends Content<String> {
        public TextContent(String text) {
            this.text = text;
        }


        @Override
        protected void localSetPoints(List<String> points) {
            throw new IllegalArgumentException("Text content can not contains Lists");
        }

        @Override
        protected void localSetText(String text) {
            this.text = text;
        }
    }

    public static class ListContent<P> extends Content<P> {
        public ListContent(List<P> points) {
            this.points = points;
        }


        @Override
        protected void localSetPoints(List<P> points) {
            this.points = points;
        }

        @Override
        protected void localSetText(String text) {
            throw new IllegalArgumentException("List content can not contains text");
        }
    }

    public static class Organization {
        private String name;
        private String position;
        private String activity;
        private Calendar start;
        private Calendar end;
        private SimpleDateFormat ft =
                new SimpleDateFormat("yyyy.MM");

        public Organization(String name, String position, String activity, Calendar start, Calendar end) {
            this.name = name;
            this.position = position;
            this.activity = activity;
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return name + "\n" + position + "\n" + ft.format(start.getTime()) + " - " + ft.format(end.getTime()) + " | " + activity;
        }
    }

    public enum ContactType {
        PHONE,
        SKYPE,
        MAIL,
        GITHUB,
        STACKOVFLW,
        LINKEDIN
    }


}
