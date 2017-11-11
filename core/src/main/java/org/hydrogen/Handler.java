package org.hydrogen;

@FunctionalInterface
public interface Handler {
    Response handle(Request request);
}
