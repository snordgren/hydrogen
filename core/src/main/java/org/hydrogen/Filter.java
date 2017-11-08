package org.hydrogen;

import java.util.function.Consumer;

@FunctionalInterface
public interface Filter {
    Handler apply(Handler handler);

    static Handler after(Handler handler, Consumer<Request> requestConsumer) {
        return request -> {
            Response response = handler.handle(request);
            requestConsumer.accept(request);
            return response;
        };
    }

    static Handler before(Handler handler, Consumer<Request> requestConsumer) {
        return request -> {
            requestConsumer.accept(request);
            return handler.handle(request);
        };
    }
}
