package it.vle.entellocore.service;

import it.vle.entellocore.model.SocialService;

public class ContentServiceFactory {

    public ContentService createContentService(SocialService social) {
        return switch (social.getExtractorType()) {
            case GALLERY_DL -> new GalleryDlService();
            case YOUTUBE_DL -> new YoutubeDlService();
            case HACKER_NEWS -> new HackerNewsService();
            case NOT_AVAILABLE -> throw new UnsupportedOperationException("Extractor not available: " + social);
        };
    }
}
