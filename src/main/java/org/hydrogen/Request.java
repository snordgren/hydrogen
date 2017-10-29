package org.hydrogen;

public class Request {
    private final String path;

    public Request(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
