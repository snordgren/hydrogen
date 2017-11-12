package org.hydrogen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ClasspathDirectory extends StaticDirectory {
    public ClasspathDirectory(String directory) {
        super(directory);
    }

    private ClassLoader getClassLoader() {
        return ClasspathDirectory.class.getClassLoader();
    }

    @Override
    public boolean isPathValid(String path) {
        return getClassLoader().getResource(buildPath(path)) != null;
    }

    @Override
    public byte[] load(String path) {
        try {
            InputStream in = getClassLoader().getResourceAsStream(buildPath(path));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read;
            while ((read = in.read()) != -1) {
                out.write(read);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
