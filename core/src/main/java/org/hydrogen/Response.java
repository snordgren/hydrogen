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
    private final Status status;
    private final Optional<Session> session;
    private final Map<String, String> headers;
    private final ContentType contentType;
    private final byte[] body;

    private Response(Status status,
            Map<String, String> headers,
            ContentType contentType,
            byte[] body,
            Optional<Session> session) {
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public static Builder found(String url) {
        return status(Status.FOUND)
                .header("Location", url);
    }

    public static Builder notFound() {
        return status(Status.NOT_FOUND);
    }

    public static Builder movedPermanently(String url) {
        return status(Status.MOVED_PERMANENTLY)
                .header("Location", url);
    }

    public static Builder ok() {
        return status(Status.OK);
    }

    public static Response redirect(String url) {
        return redirect(url, Status.FOUND);
    }

    public static Response redirect(String url, Status status) {
        return status(status)
                .header("Location", url)
                .emptyBody();
    }

    public static Builder status(Status status) {
        return new Builder(status);
    }

    public static final class Builder {
        private final Status status;
        private final Map<String, String> headers = new HashMap<>();
        private Session session = null;

        public Builder(Status status) {
            this.status = status;
        }

        public Builder header(String name, String value) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(value);
            headers.put(name, value);
            return this;
        }

        public Response body(ContentType contentType, byte[] body) {
            return new Response(
                    status,
                    headers,
                    contentType,
                    body,
                    Optional.ofNullable(session));
        }

        private Response createTextResponse(ContentType contentType, String text) {
            return new Response(
                    status,
                    headers,
                    contentType,
                    text.getBytes(UTF_8),
                    Optional.ofNullable(session));
        }

        public Response emptyBody() {
            return new Response(
                    status,
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
