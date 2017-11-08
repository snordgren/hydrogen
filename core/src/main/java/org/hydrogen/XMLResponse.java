package org.hydrogen;

public class XMLResponse extends TextualResponse {
    private XMLResponse(StatusCode statusCode, String text) {
        super(statusCode, ContentType.XML, text);
    }

    @Override
    public <T> T accept(ResponseAdapter<T> adapter) {
        return adapter.xml(this);
    }

    public static XMLResponse ok(String xml) {
        return new XMLResponse(StatusCode.OK, xml);
    }
}
