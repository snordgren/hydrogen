package org.hydrogen;

public interface ResponseAdapter<T> {
    T text(TextResponse response);
}
