package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostReddit extends ExtractedPost {

    private final String subreddit;
    private final String title;
    private final String urlExt;
    private final String upvote;
    private final String numComments;
    private final String urlExtDomain;

    public ExtractedPostReddit(
            String author,
            String text,
            List<Path> media,
            Long timestamp,
            String subreddit,
            String title,
            String urlExt,
            String urlExtDomain,
            String upvote,
            String numComments
    ) {

        super(author, text, media, timestamp);
        this.subreddit = subreddit;
        this.title = title;
        this.urlExt = urlExt;
        this.upvote = upvote;
        this.numComments = numComments;
        this.urlExtDomain = urlExtDomain;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getTitle() {
        if (this.urlExtDomain != null) {
            return title + " (" + urlExtDomain + ")";
        }
        return title;
    }

    public String getUrlExt() {
        return urlExt;
    }

    public String getUpvote() {
        return upvote;
    }

    public String getNumComments() {
        return numComments;
    }

    @Override
    public String getAuthor() {
        return "u/" + this.author;
    }
}
