package org.hydrogen;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public class Request {
    private final String url;
    private final RequestMethod method;
    private final Map<String, String> queryParams, headers;
    private final InputStream body;
    private final Session session;

    public Request(RequestMethod method, String url) {
        this(method, url, Collections.emptyMap(), Collections.emptyMap(),
                null, Session.empty());
    }

    public Request(RequestMethod method,
            String url,
            Map<String, String> queryParams,
            Map<String, String> headers,
            InputStream body,
            Session session) {
        this.method = method;
        this.url = url;
        this.queryParams = Collections.unmodifiableMap(queryParams);
        this.headers = Collections.unmodifiableMap(headers);
        this.body = body;
        this.session = session;
    }

    public InputStream getBody() {
        return body;
    }

    public String getQueryParam(String name) {
        return getQueryParams().get(name);
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getHeader(String name) {
        return getHeaders().get(name);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public Session getSession() {
        return session;
    }

    public String getUrl() {
        return url;
    }

    public boolean hasQueryParameter(String name) {
        return getQueryParams().containsKey(name);
    }

    public static Request get(String url) {
        return new Request(RequestMethod.GET, url);
    }


    public static Request post(String url) {
        return new Request(RequestMethod.POST, url);
    }

    public Request withUrl(String url) {
        return new Request(method, url, queryParams, headers, body, session);
    }

    @Override
    public String toString() {
        return getMethod().toString() + " " + getUrl();
    }
}
