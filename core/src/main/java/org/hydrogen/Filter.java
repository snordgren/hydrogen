package org.hydrogen;

@FunctionalInterface
public interface Filter {
    Handler apply(Handler handler);
}
