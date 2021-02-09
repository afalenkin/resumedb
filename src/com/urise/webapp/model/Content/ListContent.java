package com.urise.webapp.model.Content;

import java.util.List;

public class ListContent<P> extends Content<P> {
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (P point : points
        ) {
            result.append(point.toString()).append("\n");
        }
        return result.toString();
    }
}
