package org.hydrogen;

@FunctionalInterface
public interface Handler {
    Response handle(Request request);

    default VariableHandler asVariable() {
        return (request, variables) -> handle(request);
    }
}
