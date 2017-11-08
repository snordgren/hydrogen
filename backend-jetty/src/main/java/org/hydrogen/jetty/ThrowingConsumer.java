package org.hydrogen.jetty;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void acceptThrows(T value) throws Exception;
}
