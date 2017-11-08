package org.hydrogen;

public class HTMLResponse extends TextualResponse {
    private HTMLResponse(StatusCode statusCode, String text) {
        super(statusCode, ContentType.HTML, text);
    }

    @Override
    public <T> T accept(ResponseAdapter<T> adapter) {
        return adapter.html(this);
    }

    public static HTMLResponse notFound(String html) {
        return new HTMLResponse(StatusCode.NOT_FOUND, html);
    }

    public static HTMLResponse ok(String html) {
        return new HTMLResponse(StatusCode.OK, html);
    }

}
