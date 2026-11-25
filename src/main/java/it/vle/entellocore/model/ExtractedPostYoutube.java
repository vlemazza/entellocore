package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostYoutube extends ExtractedPost {

    private final String title;
    private final String channel;
    private final String channelUrl;

    public ExtractedPostYoutube(
            String title,
            String author,
            String channel,
            String channelUrl,
            List<Path> media,
            Long timestamp
    ) {
        super(author, title, media, timestamp);
        this.title = title;
        this.channel = channel;
        this.channelUrl = channelUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getChannel() {
        return channel;
    }

    public String getChannelUrl() {
        return channelUrl;
    }
}