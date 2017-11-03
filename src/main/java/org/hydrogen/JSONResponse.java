package org.hydrogen;

public class JSONResponse extends TextualResponse {
    public JSONResponse(StatusCode statusCode, String text) {
        super(statusCode, ContentType.JSON, text);
    }

    @Override
    public <T> T accept(ResponseAdapter<T> adapter) {
        return adapter.json(this);
    }

    public static JSONResponse notFound(String json) {
        return new JSONResponse(StatusCode.NOT_FOUND, json);
    }

    public static JSONResponse ok(String json) {
        return new JSONResponse(StatusCode.OK, json);
    }
}
