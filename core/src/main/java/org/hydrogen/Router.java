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

        private static String requireSlashEnclosed(String s) {
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
            Route route = new Route(req -> req.getUrl().startsWith(path) &&
                    staticDirectory.isPathValid(removePath(path, req.getUrl())),
                    req -> {
                        String filePath = removePath(path, req.getUrl());
                        if (staticDirectory.isPathValid(filePath)) {
                            byte[] bytes = staticDirectory.load(filePath);
                            String[] pathParts = filePath.split("\\.");
                            String extension = pathParts[pathParts.length - 1];
                            ContentType contentType = ContentType.of(extension);
                            return Response.ok().body(contentType, bytes);
                        } else return Response.notFound().text("404 Not Found");
                    });
            routes.add(route);
            return this;
        }

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
