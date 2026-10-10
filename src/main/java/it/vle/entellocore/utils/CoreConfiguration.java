package it.vle.entellocore.utils;

import java.nio.file.Files;
import java.nio.file.Path;

public final class CoreConfiguration {

    private static final String GALLERY_DL_CONFIG = "GALLERY_DL_CONFIG";

    private CoreConfiguration() {
    }

    public static Path galleryDlConfigPath() {
        String configuredPath = System.getenv(GALLERY_DL_CONFIG);

        if (configuredPath == null || configuredPath.isBlank()) {
            throw new IllegalStateException("Missing GALLERY_DL_CONFIG");
        }

        Path path = Path.of(configuredPath);
        if (!path.isAbsolute()) {
            throw new IllegalStateException("GALLERY_DL_CONFIG must be an absolute path");
        }

        path = path.normalize();
        if (!Files.isReadable(path)) {
            throw new IllegalStateException("Gallery-dl config is missing: " + path);
        }

        return path;
    }
}
