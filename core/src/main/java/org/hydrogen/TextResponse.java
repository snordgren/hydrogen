package org.hydrogen;

public class TextResponse extends TextualResponse {
    private TextResponse(StatusCode statusCode, String text) {
        super(statusCode, ContentType.TEXT, text);
    }

    public static TextResponse notFound(String text) {
        return new TextResponse(StatusCode.NOT_FOUND, text);
    }

    public static TextResponse ok(String text) {
        return new TextResponse(StatusCode.OK, text);
    }

    @Override
    public <T> T accept(ResponseAdapter<T> adapter) {
        return adapter.text(this);
    }
}
