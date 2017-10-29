package org.hydrogen;

public class HTMLResponse extends ContentResponse {
    private final String html;

    HTMLResponse(StatusCode statusCode, String html) {
        super(statusCode, ContentType.HTML);
        this.html = html;
    }

    public static HTMLResponse ok(String html) {
        return new HTMLResponse(StatusCode.OK, html);
    }

    @Override
    public void accept(ResponseAdapter adapter) {
        adapter.html(this);
    }

    @Override
    public byte[] getBytes() {
        return html.getBytes(UTF_8);
    }
}
