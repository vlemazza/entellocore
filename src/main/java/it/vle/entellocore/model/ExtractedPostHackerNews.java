package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostHackerNews extends ExtractedPost{

    private final String title;
    private final String url;

    private final String type;

    private final String score;


    public ExtractedPostHackerNews(
            String author,
            String text,
            List<Path> media,
            String title,
            String url,
            String type,
            String score,
            Long timestamp
    ) {

        super(author, text, media, timestamp);
        this.title = title;
        this.url = url;
        this.type = type;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getScore() {
        return score;
    }
}
