package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostPinterest extends ExtractedPost {
    private final String title;

    public ExtractedPostPinterest(String author, String text, List<Path> images, String title, Long timestamp) {
        super(author, text, images, timestamp);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
