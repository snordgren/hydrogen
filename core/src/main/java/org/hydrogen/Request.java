package org.hydrogen;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public class Request {
    private final String url;
    private final RequestMethod method;
    private final Map<String, String> headers;
    private final InputStream body;

    public Request(RequestMethod method, String url, Map<String, String> headers,
            InputStream body) {
        this.method = method;
        this.url = url;
        this.headers = Collections.unmodifiableMap(headers);
        this.body = body;
    }

    public InputStream getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
