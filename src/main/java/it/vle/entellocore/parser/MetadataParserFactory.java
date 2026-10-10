package it.vle.entellocore.parser;

import it.vle.entellocore.model.SocialService;

public class MetadataParserFactory {

    public MetadataParser getParser(SocialService service) {
        return switch (service) {
            case TWITTER -> new TwitterMetadataParser();
            case BLUESKY -> new BlueskyMetadataParser();
            case TIKTOK -> new TiktokMetadataParser();
            case INSTAGRAM -> new InstagramMetadataParser();
            case REDDIT -> new RedditMetadataParser();
            case BILIBILI -> new BilibiliMetadataParser();
            case PIXIV -> new PixivMetadataParser();
            case AO3 -> new AO3MetadataParser();
            case POIPIKU -> new PoipikuMetadataParser();
            case TUMBLR -> new TumblrMetadataParser();
            case PINTEREST -> new PinterestMetadataParser();
            case YOUTUBE -> new YoutubeMetadataParser();
            case HACKER_NEWS, NOT_AVAILABLE -> throw new UnsupportedOperationException("Unsupported service: " + service);
        };
    }
}
