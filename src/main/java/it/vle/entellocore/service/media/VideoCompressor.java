package it.vle.entellocore.service.media;

import java.nio.file.Path;

public interface VideoCompressor {

    Path compress(Path source, long maxSizeBytes);
}