package org.hydrogen.util;

public interface ThrowingRunnable extends Runnable {
    default void run() {
        try {
            runThrows();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void runThrows() throws Exception;
}
