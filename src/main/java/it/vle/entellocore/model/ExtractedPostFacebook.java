package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostFacebook extends ExtractedPost {

    private final String type;

    public ExtractedPostFacebook(String author, String text, List<Path> media, Long timestamp, String type) {
        super(author, text, media, timestamp);
        this.type = type;
    }

    public boolean isVideo() {
        return "video".equalsIgnoreCase(type);
    }

    public ExtractedPostFacebook withDescription(String description) {
        ExtractedPostFacebook enrichedPost = new ExtractedPostFacebook(
                author,
                description,
                getMedia(),
                getTimestamp(),
                type
        );
        copyTemporaryDirectoryTo(enrichedPost);
        return enrichedPost;
    }
}
