package org.hydrogen;

import java.util.Map;

@FunctionalInterface
public interface VariableHandler {
    default Handler prepare(Map<String, String> variables) {
        return request -> handle(request, variables);
    }

    Response handle(Request request, Map<String, String> variables);
}
