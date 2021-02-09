package com.urise.webapp.model.Content;

import java.util.List;

public class TextContent extends Content<String> {
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

    @Override
    public String toString() {
        return text;
    }
}
