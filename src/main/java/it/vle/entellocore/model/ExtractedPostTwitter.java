package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostTwitter extends ExtractedPost{
    private final String replyCount;
    private final String retweetCount;
    private final String quoteCount;
    private final String viewCount;
    private final String profileImageUrl;
    private final String username;

    public ExtractedPostTwitter(
            String author,
            String text,
            List<Path> media,
            Long timestamp,
            String replyCount,
            String retweetCount,
            String quoteCount,
            String viewCount,
            String profileImageUrl,
            String username
    ) {

        super(author, text, media, timestamp);
        this.replyCount = replyCount;
        this.retweetCount = retweetCount;
        this.quoteCount = quoteCount;
        this.viewCount = viewCount;
        this.profileImageUrl = profileImageUrl;
        this.username = username;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public String getRetweetCount() {
        return retweetCount;
    }

    public String getQuoteCount() {
        return quoteCount;
    }

    public String getViewCount() {
        return viewCount;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getUrlProfile() {
        return "https://x.com/" + username;
    }

    @Override
    public String getAuthor() {
        return this.author + " (@" + username + ")";
    }

}
