package com.urise.webapp.model.Content;

public class TextContent extends Content<String> {
    public TextContent(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text + "\n";
    }
}
