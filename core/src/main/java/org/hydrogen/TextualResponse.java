package org.hydrogen;

import java.nio.charset.Charset;

public abstract class TextualResponse extends Response {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final ContentType contentType;
    private final String text;

    TextualResponse(StatusCode statusCode, ContentType contentType, String text) {
        super(statusCode);
        this.contentType = contentType;
        this.text = text;
    }

    @Override
    public final byte[] getBytes() {
        return text.getBytes(UTF_8);
    }

    public final ContentType getContentType() {
        return contentType;
    }
}
