package org.hydrogen;

import java.util.Optional;

public enum ContentType {
    BMP("image/bmp", "bmp"),
    CSS("text/css; charset=utf-8", "css"),
    CSV("text/csv; charset=utf-8", "csv"),
    GIF("image/gif", "gif"),
    HTML("text/html; charset=utf-8", "html", "htm"),
    JPEG("image/jpeg", "jpg", "jpeg", "jpe"),
    JSON("application/json; charset=utf-8", "json"),
    MPEG("audio/mpeg", "mpga", "mp2", "mp2a", "mp3", "m2a", "m3a"),
    OGG("audio/ogg", "oga", "ogg", "spx"),
    PDF("application/pdf", "pdf"),
    PNG("image/png", "png"),
    SVG("image/svg+xml", "svg", "svgz"),
    TEXT("text/plain; charset=utf-8", "text", "txt", "conf", "def", "list", "log", "in"),
    TIFF("image/tiff", "tif", "tiff"),
    XML("application/xml; charset=utf-8", "xml", "xsl"),
    ZIP("application/zip", "zip");

    private final String text;
    private final String[] extensions;

    ContentType(String text, String... extensions) {
        this.text = text;
        this.extensions = extensions;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public String getText() {
        return text;
    }

    public static Optional<ContentType> of(String extension) {
        for (ContentType contentType : values()) {
            for (String contentExt : contentType.extensions) {
                if (contentExt.equalsIgnoreCase(extension)) {
                    return Optional.of(contentType);
                }
            }
        }

        return Optional.empty();
    }
}
