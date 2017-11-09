package org.hydrogen;

import java.util.Map;
import java.util.Optional;

/**
 * A route is a handler that may or may not return a result depending on the
 * URL and HTTP method.
 */
@FunctionalInterface
public interface Route {

    Optional<Response> check(Request request);

    static Route match(RequestMethod method, String url, Handler handler) {
        Pattern pattern = new Pattern(url);
        return req -> {
            if (req.getMethod() == method) {
                Optional<Map<String, String>> variableMap = pattern.match(req.getUrl());
                return variableMap.map(vars -> handler.handle(req));
            } else return Optional.empty();
        };
    }

    static Route matchVariable(RequestMethod method, String url,
            VariableHandler handler) {
        Pattern pattern = new Pattern(url);
        return req -> {
            if (req.getMethod() == method) {
                Optional<Map<String, String>> variableMap = pattern.match(req.getUrl());
                return variableMap.map(vars -> handler.handle(req, vars));
            } else return Optional.empty();
        };
    }
}
