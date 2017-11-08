package org.hydrogen;

import java.io.InputStream;

public class Request {
    private final String url;
    private final RequestMethod method;
    private final String[] headers;
    private final InputStream body;

    public Request(RequestMethod method, String url, String[] headers, InputStream body) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
    }

    public InputStream getBody() {
        return body;
    }

    public String[] getHeaders() {
        return headers;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
