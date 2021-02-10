package com.urise.webapp.model.Content;

import java.util.List;

public class ListContent<P> extends Content<P> {
    public ListContent(List<P> points) {
        this.points = points;
    }
    public ListContent(String text, List<P> points) {
        this(points);
        this.text = text;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (text != null && !text.isEmpty()) {
            result.append(text).append("\n");
        }
        for (P point : points) {
            result.append(point.toString()).append("\n");
        }
        return result.toString();
    }
}
