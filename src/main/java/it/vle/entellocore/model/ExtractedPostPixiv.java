package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostPixiv extends ExtractedPost {

    private final String title;

    public ExtractedPostPixiv(String author, String text, List<Path> media, Long timestamp, String title) {
        super(author, text, media, timestamp);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
