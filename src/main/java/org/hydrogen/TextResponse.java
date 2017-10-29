package org.hydrogen;

public class TextResponse extends ContentResponse {
    private final String text;

    private TextResponse(StatusCode statusCode, String text) {
        super(statusCode, ContentType.PLAIN_TEXT);
        this.text = text;
    }

    public static TextResponse ok(String text) {
        return new TextResponse(StatusCode.OK, text);
    }

    @Override
    public void accept(ResponseAdapter adapter) {
        adapter.text(this);
    }

    @Override
    public byte[] getBytes() {
        return text.getBytes(UTF_8);
    }
}
