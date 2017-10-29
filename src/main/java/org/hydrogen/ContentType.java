package org.hydrogen;

public enum ContentType {
    HTML("text/html; charset=utf-8"),
    PLAIN_TEXT("text/plain; charset=utf-8");

    private final String text;

    ContentType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
