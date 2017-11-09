package org.hydrogen;

public abstract class AbstractStaticDirectory implements StaticDirectory {
    private final String directory;

    public AbstractStaticDirectory(String directory) {
        this.directory = removeSlashSuffix(directory);
    }

    protected String buildPath(String path) {
        if (directory.isEmpty()) {
            return path;
        } else {
            return directory + ensureSlashPrefix(path);
        }
    }

    private String ensureSlashPrefix(String path) {
        if (path.startsWith("/")) {
            return path;
        } else {
            return "/" + path;
        }
    }

    private String removeSlashSuffix(String path) {
        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        } else {
            return path;
        }
    }
}
