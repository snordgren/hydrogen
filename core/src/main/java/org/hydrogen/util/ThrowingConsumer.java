package org.hydrogen.util;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void acceptThrows(T value) throws Exception;
}
