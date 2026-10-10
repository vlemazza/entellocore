package it.vle.entellocore.service.media;

import it.vle.entellocore.model.ExtractedPost;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class MediaProcessor {

    private static final Set<String> VIDEO_EXTENSIONS = Set.of(
            "avi", "m4v", "mkv", "mov", "mp4", "webm"
    );

    private final VideoCompressor videoCompressor;

    public MediaProcessor() {
        this(new FfmpegVideoCompressor());
    }

    public MediaProcessor(VideoCompressor videoCompressor) {
        this.videoCompressor = Objects.requireNonNull(videoCompressor);
    }

    public void process(ExtractedPost post, long maxFileSizeBytes) {
        Objects.requireNonNull(post);
        if (maxFileSizeBytes <= 0) {
            throw new IllegalArgumentException("Maximum file size must be greater than zero");
        }
        if (post.hasNoMedia()) {
            return;
        }

        List<Path> processedMedia = new ArrayList<>(post.getMedia().size());
        boolean mediaChanged = false;

        for (Path media : post.getMedia()) {
            Path path = Objects.requireNonNull(media);
            long size;
            try {
                size = Files.size(path);
            } catch (IOException e) {
                throw new MediaProcessingException("Unable to read media file size: " + path, e);
            }

            if (size > maxFileSizeBytes && this.isVideo(path)) {
                Path compressed = videoCompressor.compress(path, maxFileSizeBytes);
                if (compressed == null || !Files.isRegularFile(compressed)) {
                    throw new MediaProcessingException("Video compressor did not produce a file for: " + path);
                }
                try {
                    if (Files.size(compressed) > maxFileSizeBytes) {
                        throw new MediaProcessingException("Compressed video still exceeds the size limit: " + path);
                    }
                } catch (IOException e) {
                    throw new MediaProcessingException("Unable to verify compressed video: " + compressed, e);
                }
                processedMedia.add(compressed);
                mediaChanged = true;
            } else {
                processedMedia.add(path);
            }
        }

        if (mediaChanged) {
            post.replaceMedia(processedMedia);
        }
    }

    private boolean isVideo(Path path) {
        String filename = path.getFileName().toString();
        int extensionStart = filename.lastIndexOf('.');
        return extensionStart >= 0 && VIDEO_EXTENSIONS.contains(
                filename.substring(extensionStart + 1).toLowerCase(Locale.ROOT)
        );
    }
}