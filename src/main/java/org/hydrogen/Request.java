package org.hydrogen;

public class Request {
    private final String path;
    private final RequestMethod method;

    public Request(RequestMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
