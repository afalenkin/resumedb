package com.urise.webapp.model.Content;

import java.util.List;

public abstract class Content<C> {
    protected String text;
    protected List<C> points;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (text != null && !text.isEmpty()) {
            result.append(text).append("\n");
        }
        if (points != null && !points.isEmpty()) {
            result.append(points.toString());
        }
        return result.toString();
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
