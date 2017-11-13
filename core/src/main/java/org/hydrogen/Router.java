package org.hydrogen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            Optional<Response> result = route.check(request);
            if (result.isPresent()) {
                return result.get();
            }
        }
        return notFound.handle(request);
    }

    public static final class Builder {
        private final List<Route> routes = new ArrayList<>();
        private Handler notFound;

        private static String requireSlashEnclosed(String s) {
            if (s == null || s.isEmpty() || s.equals("/")) {
                return "/";
            }

            String enclosure = "/";
            StringBuilder b = new StringBuilder();
            if (!s.startsWith(enclosure)) {
                b.append(enclosure);
            }
            b.append(s);
            if (!s.endsWith(enclosure)) {
                b.append(enclosure);
            }
            return b.toString();
        }

        private String removePath(String path, String str) {
            return str.substring(path.length());
        }

        public Builder bind(String passedPath, StaticDirectory staticDirectory) {
            String path = requireSlashEnclosed(passedPath);
            Route route = req -> {
                if (req.getUrl().startsWith(path)) {
                    return staticDirectory.check(removePath(path, req.getUrl()));
                } else return Optional.empty();
            };
            routes.add(route);
            return this;
        }

        /**
         * Instantiates a new router from the defined not-found handler and
         * routes.
         *
         * @return The new router.
         */
        public Router build() {
            return new Router(notFound, routes);
        }

        public Builder get(String url, Handler handler) {
            routes.add(Route.match(RequestMethod.GET, url, handler));
            return this;
        }

        /**
         * Adds a handler for a group of routes. The handler is passed a
         * request with its URL truncated to remove the starting path,
         * which is shared by all routes in the group.
         *
         * @param passedPath The path.
         * @param handler The handler for the group.
         * @return This builder, for chaining.
         */
        public Builder group(String passedPath, Handler handler) {
            String path = requireSlashEnclosed(passedPath);
            Route route = req -> {
                if (req.getUrl().startsWith(path)) {
                    String newUrl = removePath(path, req.getUrl());
                    return Optional.of(handler.handle(req.withUrl(newUrl)));
                } else return Optional.empty();
            };
            routes.add(route);
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
