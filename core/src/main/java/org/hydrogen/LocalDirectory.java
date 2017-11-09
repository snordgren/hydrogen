package org.hydrogen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LocalDirectory extends AbstractStaticDirectory {
    public LocalDirectory(String directory) {
        super(directory);
    }

    @Override
    public boolean isPathValid(String path) {
        return new File(buildPath(path)).exists();
    }

    @Override
    public byte[] load(String path) {
        try {
            return Files.readAllBytes(new File(buildPath(path)).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
