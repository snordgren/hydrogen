package org.hydrogen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class Router implements Handler {
    private final List<Route> routes;
    private final Handler notFound;

    private Router(Handler notFound, List<Route> routes) {
        this.notFound = notFound;
        this.routes = routes;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Response handle(Request request) {
        for (Route route : routes) {
            if (route.getPredicate().test(request)) {
                return route.getHandler().handle(request);
            }
        }
        return notFound.handle(request);
    }

    private boolean isRouteMatch(String route, String url) {
        return route.equalsIgnoreCase(url);
    }

    public static final class Builder {
        private final List<Route> routes = new ArrayList<>();
        private Handler notFound;

        public Router build() {
            return new Router(notFound, routes);
        }

        public Builder get(String url, Handler handler) {
            routes.add(Route.match(RequestMethod.GET, url, handler));
            return this;
        }

        public Builder group(String url, Handler handler) {
            Predicate<Request> predicate = req -> req.getUrl().startsWith(url);
            Handler modifiedHandler = req -> {
                String newUrl = req.getUrl().substring(url.length());
                return handler.handle(req.withUrl(newUrl));
            };
            routes.add(new Route(predicate, modifiedHandler));
            return this;
        }

        public Builder notFound(Handler notFound) {
            this.notFound = notFound;
            return this;
        }

        public Builder post(String url, Handler handler) {
            routes.add(Route.match(RequestMethod.POST, url, handler));
            return this;
        }
    }
}
