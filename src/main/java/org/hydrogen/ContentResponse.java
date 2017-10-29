package org.hydrogen;

public abstract class ContentResponse extends Response {
    private final ContentType contentType;

    ContentResponse(StatusCode statusCode, ContentType contentType) {
        super(statusCode);
        this.contentType = contentType;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
