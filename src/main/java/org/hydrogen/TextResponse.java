package org.hydrogen;

import java.nio.charset.Charset;

public class TextResponse extends ContentResponse {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final String text;

    private TextResponse(StatusCode statusCode, String text) {
        super(statusCode, ContentType.PLAIN_TEXT);
        this.text = text;
    }

    @Override
    public void accept(ResponseAdapter adapter) {
        adapter.text(this);
    }

    @Override
    public byte[] getBytes() {
        return text.getBytes(UTF_8);
    }

    public static TextResponse ok(String text) {
        return new TextResponse(StatusCode.OK, text);
    }
}
