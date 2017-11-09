package org.hydrogen;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable, type-safe representation of an HTTP response.
 *
 * @author Silas Nordgren
 */
public final class Response {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final StatusCode statusCode;
    private final Optional<Session> session;
    private final Map<String, String> headers;
    private final ContentType contentType;
    private final byte[] body;

    private Response(StatusCode statusCode,
            Map<String, String> headers,
            ContentType contentType,
            byte[] body,
            Optional<Session> session) {
        this.statusCode = statusCode;
        this.headers = Collections.unmodifiableMap(headers);
        this.contentType = contentType;
        this.body = body;
        this.session = session;
    }

    public byte[] getBody() {
        return body;
    }

    public String getBodyAsString() {
        return new String(getBody(), UTF_8);
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Optional<Session> getSession() {
        return session;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public static Builder found(String url) {
        return status(StatusCode.FOUND)
                .header("Location", url);
    }

    public static Builder notFound() {
        return status(StatusCode.NOT_FOUND);
    }

    public static Builder movedPermanently(String url) {
        return status(StatusCode.MOVED_PERMANENTLY)
                .header("Location", url);
    }

    public static Builder ok() {
        return status(StatusCode.OK);
    }

    public static Response redirect(String url) {
        return redirect(url, StatusCode.FOUND);
    }

    public static Response redirect(String url, StatusCode statusCode) {
        return status(statusCode)
                .header("Location", url)
                .emptyBody();
    }

    public static Builder status(StatusCode statusCode) {
        return new Builder(statusCode);
    }

    public static final class Builder {
        private final StatusCode statusCode;
        private final Map<String, String> headers = new HashMap<>();
        private Session session = null;

        public Builder(StatusCode statusCode) {
            this.statusCode = statusCode;
        }

        public Builder header(String name, String value) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(value);
            headers.put(name, value);
            return this;
        }

        public Response body(ContentType contentType, byte[] body) {
            return new Response(
                    statusCode,
                    headers,
                    contentType,
                    body,
                    Optional.ofNullable(session));
        }

        private Response createTextResponse(ContentType contentType, String text) {
            return new Response(
                    statusCode,
                    headers,
                    contentType,
                    text.getBytes(UTF_8),
                    Optional.ofNullable(session));
        }

        public Response emptyBody() {
            return new Response(
                    statusCode,
                    headers,
                    ContentType.HTML,
                    new byte[0],
                    Optional.ofNullable(session));
        }

        public Response html(String text) {
            return createTextResponse(ContentType.HTML, text);
        }

        public Response json(String text) {
            return createTextResponse(ContentType.JSON, text);
        }

        public Builder session(Session session) {
            Objects.requireNonNull(session);
            this.session = session;
            return this;
        }

        public Response text(String text) {
            return createTextResponse(ContentType.TEXT, text);
        }

        public Response xml(String text) {
            return createTextResponse(ContentType.XML, text);
        }
    }
}
