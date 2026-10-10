package it.vle.entellocore.service;

import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.SocialService;

import java.util.Optional;

public class EntelloExtractorEngine implements ContentExtractionEngine {

    private final ContentServiceFactory serviceFactory;

    public EntelloExtractorEngine() {
        this(new ContentServiceFactory());
    }

    public EntelloExtractorEngine(ContentServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public Optional<ExtractedPost> process(String rawUrl) {
        SocialService service = SocialService.fromUrl(rawUrl);
        if (service == SocialService.NOT_AVAILABLE) {
            return Optional.empty();
        }

        String normalizedUrl = service.normalizeTwitterUrl(rawUrl);
        ContentService extractor = serviceFactory.createContentService(service);
        ExtractedPost post = extractor.extract(normalizedUrl, service);

        return Optional.ofNullable(post);
    }

}
