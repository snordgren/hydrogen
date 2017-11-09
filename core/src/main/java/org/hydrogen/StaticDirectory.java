package org.hydrogen;

public interface StaticDirectory {
    boolean isPathValid(String path);

    byte[] load(String path);
}
