package org.hydrogen;

import java.util.Map;

@FunctionalInterface
public interface VariableHandler {
    Response handle(Request request, Map<String, String> variables);

    default Handler prepare(Map<String, String> variables) {
        return request -> handle(request, variables);
    }
}
