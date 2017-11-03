package org.hydrogen;

/**
 * As Java lacks case classes and pattern matching, we have to hand-roll it for
 * the Response type using the adapter pattern. This works because the Response
 * type cannot be inherited of outside of this package. If more arguments than
 * the Response object are needed, a new class can be created whose constructor
 * arguments work like a partial invocation of the adapter.
 *
 * @param <T> The type of value to return of the adapter invocation.
 */
public interface ResponseAdapter<T> {
    T html(HTMLResponse response);

    T json(JSONResponse response);

    T text(TextResponse response);

    T xml(XMLResponse response);
}
