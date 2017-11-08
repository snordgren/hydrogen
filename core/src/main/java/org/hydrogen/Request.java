package org.hydrogen;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public class Request {
    private final String url;
    private final RequestMethod method;
    private final Map<String, String> headers;
    private final InputStream body;

    public Request(RequestMethod method, String url) {
        this(method, url, Collections.emptyMap(), null);
    }

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

    public static Request get(String url) {
        return new Request(RequestMethod.GET, url);
    }

    public static Request post(String url) {
        return new Request(RequestMethod.POST, url);
    }

    public Request withUrl(String url) {
        return new Request(method, url, headers, body);
    }

    @Override
    public String toString() {
        return getMethod().toString() + " " + getUrl();
    }
}
