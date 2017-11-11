package org.hydrogen;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Request {
    private final String url;
    private final RequestMethod method;
    private final Map<String, String> routeParams, queryParams, headers;
    private final InputStream body;
    private final Session session;

    private Request(RequestMethod method, String url) {
        this(method,
                url,
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                null,
                Session.empty());
    }

    public Request(RequestMethod method,
            String url,
            Map<String, String> routeParams,
            Map<String, String> queryParams,
            Map<String, String> headers,
            InputStream body,
            Session session) {
        this.method = method;
        this.url = url;
        this.routeParams = Collections.unmodifiableMap(routeParams);
        this.queryParams = Collections.unmodifiableMap(queryParams);
        this.headers = Collections.unmodifiableMap(headers);
        this.body = body;
        this.session = session;
    }

    public InputStream getBody() {
        return body;
    }

    /**
     * Returns a builder pre-configured with the values of this request as the
     * default.
     *
     * @return The builder to use.
     */
    public Builder getBuilder() {
        return builder()
                .method(method)
                .headers(headers)
                .routeParams(routeParams)
                .queryParams(queryParams)
                .body(body)
                .session(session);
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

    public String getRouteParam(String name) {
        return routeParams.get(name);
    }

    public Map<String, String> getRouteParams() {
        return routeParams;
    }

    public Session getSession() {
        return session;
    }

    public String getUrl() {
        return url;
    }

    public boolean hasHeader(String name) {
        return headers.containsKey(name);
    }

    public boolean hasQueryParam(String name) {
        return queryParams.containsKey(name);
    }

    public boolean hasRouteParam(String name) {
        return routeParams.containsKey(name);
    }

    public Request withRouteParams(Map<String, String> routeParams) {
        return getBuilder().routeParams(routeParams).build();
    }

    public Request withUrl(String url) {
        return getBuilder().url(url).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Request get(String url) {
        return new Request(RequestMethod.GET, url);
    }

    public static Request post(String url) {
        return new Request(RequestMethod.POST, url);
    }

    public static class Builder {
        private String url;
        private RequestMethod method;
        private final Map<String, String> routeParams = new HashMap<>(),
                queryParams = new HashMap<>(),
                headers = new HashMap<>();
        private InputStream body;
        private Session session;

        public Builder body(InputStream body) {
            this.body = body;
            return this;
        }

        public Request build() {
            return new Request(method, url, routeParams, queryParams, headers,
                    body, session);
        }

        public Builder header(String header, String value) {
            headers.put(header, value);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers.clear();
            this.headers.putAll(headers);
            return this;
        }

        public Builder method(RequestMethod method) {
            this.method = method;
            return this;
        }

        public Builder queryParam(String param, String value) {
            queryParams.put(param, value);
            return this;
        }

        public Builder queryParams(Map<String, String> queryParams) {
            this.queryParams.clear();
            this.queryParams.putAll(queryParams);
            return this;
        }

        public Builder routeParam(String param, String value) {
            routeParams.put(param, value);
            return this;
        }

        public Builder routeParams(Map<String, String> routeParams) {
            this.routeParams.clear();
            this.routeParams.putAll(routeParams);
            return this;
        }

        public Builder session(Session session) {
            this.session = session;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }
    }
}
