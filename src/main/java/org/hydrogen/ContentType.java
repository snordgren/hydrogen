package org.hydrogen;

public enum ContentType {
    HTML("text/html; charset=utf-8"),
    JSON("application/json; charset=utf-8"),
    TEXT("text/plain; charset=utf-8"),
    XML("application/xml; charset=utf-8");

    private final String text;

    ContentType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
