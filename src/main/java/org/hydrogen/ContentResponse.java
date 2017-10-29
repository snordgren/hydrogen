package org.hydrogen;

import java.nio.charset.Charset;

public abstract class ContentResponse extends Response {
    protected static final Charset UTF_8 = Charset.forName("UTF-8");
    private final ContentType contentType;

    ContentResponse(StatusCode statusCode, ContentType contentType) {
        super(statusCode);
        this.contentType = contentType;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
