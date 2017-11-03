package org.hydrogen;

import java.util.HashMap;
import java.util.Map;

public class Router implements Handler {
    private final Map<String, Handler> routes;
    private final Handler notFoundHandler;

    private Router(Handler notFoundHandler) {
        this(notFoundHandler, new HashMap<>());
    }

    private Router(Handler notFoundHandler, Map<String, Handler> routes) {
        this.notFoundHandler = notFoundHandler;
        this.routes = routes;
    }

    public Router get(String route, Handler handler) {
        Map<String, Handler> newMap = new HashMap<>();
        newMap.putAll(routes);
        newMap.put(route, handler);
        return new Router(notFoundHandler, newMap);
    }

    @Override
    public Response handle(Request request) {
        Handler handler = routes.keySet().stream()
                .filter(route -> route.equalsIgnoreCase(request.getPath()))
                .findAny()
                .map(routes::get)
                .orElse(notFoundHandler);
        return handler.handle(request);
    }

    public static Router of(Handler handler) {
        return new Router(handler);
    }
}
