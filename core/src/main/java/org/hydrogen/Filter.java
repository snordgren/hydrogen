package org.hydrogen;

/**
 * A filter is a function transforming one handler into another.
 */
@FunctionalInterface
public interface Filter {

    /**
     * Applies this filter to a handler, creating a new handler that works
     * differently.
     *
     * @param handler The handler that is to be transformed.
     * @return The transformed handler, with different or additional behavior.
     */
    Handler apply(Handler handler);
}
