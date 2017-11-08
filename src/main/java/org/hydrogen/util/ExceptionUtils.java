package org.hydrogen.util;

public class ExceptionUtils {
    public static void run(ThrowingRunnable runnable) {
        runnable.run();
    }
}
