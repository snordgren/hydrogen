package org.hydrogen;

import java.util.function.Predicate;

public class Route {
    private final Predicate<Request> predicate;
    private final Handler handler;

    public Route(Predicate<Request> predicate, Handler handler) {
        this.handler = handler;
        this.predicate = predicate;
    }

    public Handler getHandler() {
        return handler;
    }

    public Predicate<Request> getPredicate() {
        return predicate;
    }

    public static Route match(RequestMethod method, String path, Handler handler) {
        return new Route(req -> req.getMethod() == method &&
                req.getUrl().equalsIgnoreCase(path),
                handler);
    }
}
