package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostTiktok extends ExtractedPost{
    public ExtractedPostTiktok(String author, String text, List<Path> media, Long timestamp) {
        super(author, text, media, timestamp);
    }

}
