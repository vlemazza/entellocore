package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostTumblr extends ExtractedPost{

    private final List<String> tags;
    public ExtractedPostTumblr(String author, String text, List<Path> media, Long timestamp, List<String> tags) {
        super(author, text, media, timestamp);
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
