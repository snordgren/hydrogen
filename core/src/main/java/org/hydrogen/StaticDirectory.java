package org.hydrogen;

import java.util.Optional;

public abstract class StaticDirectory {
    private final String directory;

    public StaticDirectory(String directory) {
        this.directory = removeSlashSuffix(directory);
    }

    protected String buildPath(String path) {
        if (directory.isEmpty()) {
            return path;
        } else {
            return directory + ensureSlashPrefix(path);
        }
    }

    public Optional<Response> check(String filePath) {
        if (isPathValid(filePath) && filePath.contains(".")) {
            byte[] bytes = load(filePath);
            String[] pathParts = filePath.split("\\.");
            String extension = pathParts[pathParts.length - 1];
            ContentType contentType = ContentType.of(extension)
                    .orElseThrow(() -> {
                        String s = "Unable to deduce MIME type of " + filePath + ".";
                        return new RuntimeException(s);
                    });
            return Optional.of(Response.ok().body(contentType, bytes));
        } else return Optional.empty();
    }

    private String ensureSlashPrefix(String path) {
        if (path.startsWith("/")) {
            return path;
        } else {
            return "/" + path;
        }
    }

    protected abstract boolean isPathValid(String path);

    protected abstract byte[] load(String path);

    private String removeSlashSuffix(String path) {
        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        } else {
            return path;
        }
    }
}
