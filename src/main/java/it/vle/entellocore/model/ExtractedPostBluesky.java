package it.vle.entellocore.model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ExtractedPostBluesky extends ExtractedPost {

    private final ExtractedPostBluesky embeddedPost;
    private final String embeddedPostUri;

    private final EmbedExternal embedExternal;
    private final String avatar;

    private final String replyCount;
    private final String repostCount;

    private final String likeCount;

    private final String username;

    public ExtractedPostBluesky(
            String author,
            String text,
            List<Path> media,
            Long timestamp,
            ExtractedPostBluesky embeddedPost,
            String embeddedPostUri,
            EmbedExternal embedExternal,
            String avatar,
            String replyCount,
            String repostCount,
            String likeCount,
            String username
    ) {

        super(author, text, media, timestamp);
        this.embeddedPost = embeddedPost;
        this.embeddedPostUri = embeddedPostUri;
        this.embedExternal = embedExternal;
        this.avatar = avatar;
        this.replyCount = replyCount;
        this.repostCount = repostCount;
        this.likeCount = likeCount;
        this.username = username;

    }

    public ExtractedPost getEmbeddedPost() {
        return embeddedPost;
    }

    public boolean hasEmbeddedPost() {
        return embeddedPost != null;
    }

    public String getEmbeddedPostUri() {
        return embeddedPostUri;
    }

    public ExtractedPostBluesky withEmbeddedPost(ExtractedPostBluesky post) {
        ExtractedPostBluesky enrichedPost = new ExtractedPostBluesky(
                author,
                getText(),
                getMedia(),
                getTimestamp(),
                post,
                embeddedPostUri,
                embedExternal,
                avatar,
                replyCount,
                repostCount,
                likeCount,
                username
        );
        copyTemporaryDirectoryTo(enrichedPost);
        return enrichedPost;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public String getRepostCount() {
        return repostCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    @Override
    public String getAuthor() {
        return author + " (" + username + ")";
    }

    public record EmbedExternal(String description, String title, String uri) {
    }

    public EmbedExternal getEmbedExternal() {
        return embedExternal;
    }

    @Override
    public void close() throws IOException {
        super.close();

        if (embeddedPost != null) {
            embeddedPost.close();
        }
    }

}
