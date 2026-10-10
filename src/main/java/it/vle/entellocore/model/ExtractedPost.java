package it.vle.entellocore.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ExtractedPost implements AutoCloseable {

    protected final String author;
    private final String text;
    private List<Path> media;
    private final Long timestamp;
    private final Set<Path> temporaryDirectories = new LinkedHashSet<>();

    public ExtractedPost(String author, String text, List<Path> media, Long timestamp) {
        this.author = author;
        this.text = text;
        this.media = media;
        this.timestamp = timestamp;
    }


    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public List<Path> getMedia() {
        return media;
    }

    public void replaceMedia(List<Path> media) {
        this.media = media;
    }

    public void addTemporaryDirectory(Path directory) {
        Path normalized = directory.toAbsolutePath().normalize();
        temporaryDirectories.add(normalized);
    }

    protected void copyTemporaryDirectoryTo(ExtractedPost target) {
        target.temporaryDirectories.addAll(temporaryDirectories);
        temporaryDirectories.clear();
    }

    @Override
    public void close() throws IOException {
        Iterator<Path> directories = temporaryDirectories.iterator();

        while (directories.hasNext()) {
            Path directory = directories.next();

            try (var paths = Files.walk(directory)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                    Files.deleteIfExists(path);
                }
            }

            directories.remove();
        }
    }

    public boolean hasNoMedia() {
        return media == null || media.isEmpty();
    }

    public Long getTimestamp() {
        return timestamp;
    }

}