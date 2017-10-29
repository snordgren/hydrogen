package org.hydrogen;

/**
 * Immutable, type-safe representation of an HTTP response.
 *
 * @author Silas Nordgren
 */
public abstract class Response {
    private final StatusCode statusCode;

    Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public abstract void accept(ResponseAdapter adapter);

    public abstract byte[] getBytes();

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public static Response plainText(String html) {
        throw new UnsupportedOperationException();
    }
}
