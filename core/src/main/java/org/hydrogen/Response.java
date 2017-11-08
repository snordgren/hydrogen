package org.hydrogen;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable, type-safe representation of an HTTP response.
 *
 * @author Silas Nordgren
 */
public class Response {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final StatusCode statusCode;
    private final Map<String, String> headers;
    private final ContentType contentType;
    private final byte[] body;

    private Response(StatusCode statusCode,
            Map<String, String> headers,
            ContentType contentType,
            byte[] body) {
        this.statusCode = statusCode;
        this.headers = Collections.unmodifiableMap(headers);
        this.contentType = contentType;
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public static Builder notFound() {
        return new Builder(StatusCode.NOT_FOUND);
    }

    public static Builder ok() {
        return new Builder(StatusCode.OK);
    }

    public static class Builder {
        private final StatusCode statusCode;
        private final Map<String, String> headers = new HashMap<>();

        public Builder(StatusCode statusCode) {
            this.statusCode = statusCode;
        }

        public Builder header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        private Response createTextResponse(ContentType contentType, String text) {
            return new Response(
                    statusCode,
                    headers,
                    contentType,
                    text.getBytes());
        }

        public Response html(String text) {
            return createTextResponse(ContentType.HTML, text);
        }

        public Response json(String text) {
            return createTextResponse(ContentType.JSON, text);
        }

        public Response text(String text) {
            return createTextResponse(ContentType.TEXT, text);
        }

        public Response xml(String text) {
            return createTextResponse(ContentType.XML, text);
        }
    }
}
