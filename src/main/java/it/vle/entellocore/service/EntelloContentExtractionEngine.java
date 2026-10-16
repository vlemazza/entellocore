package it.vle.entellocore.service;

import it.vle.entellocore.exception.ContentExtractionException;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.SocialService;
import it.vle.entellocore.model.TextExtractionOutcome;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntelloContentExtractionEngine
        implements ContentExtractionEngine {

    private final ContentServiceFactory serviceFactory;
    private final SocialUrlResolver urlResolver =
            new SocialUrlResolver();

    public EntelloContentExtractionEngine() {
        this(new ContentServiceFactory());
    }

    public EntelloContentExtractionEngine(ContentServiceFactory serviceFactory) {

        this.serviceFactory = serviceFactory;
    }

    @Override
    public List<TextExtractionOutcome> extractFromText(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<TextExtractionOutcome> outcomes = new ArrayList<>();

        for (String rawUrl : SocialUrlExtractor.extractUrls(text)) {
            Optional<SocialUrlResolver.ResolvedSocialUrl> resolution = urlResolver.resolve(rawUrl);

            if (resolution.isEmpty()) {
                continue;
            }

            SocialUrlResolver.ResolvedSocialUrl resolved = resolution.get();

            try {
                ExtractedPost post = extractPost(resolved);

                outcomes.add(
                        new TextExtractionOutcome.Success(
                                resolved.originalUrl(),
                                post
                        )
                );
            } catch (ContentExtractionException exception) {
                outcomes.add(
                        new TextExtractionOutcome.Failure(
                                resolved.originalUrl(),
                                exception
                        )
                );
            }
        }

        return List.copyOf(outcomes);
    }

    private ExtractedPost extractPost(SocialUrlResolver.ResolvedSocialUrl resolved) throws ContentExtractionException {

        SocialService service = resolved.service();

        ContentService extractor = serviceFactory.createContentService(service);

        ExtractedPost post = extractor.extract(resolved.normalizedUrl(), service);

        if (post == null) {
            throw new ContentExtractionException("Extractor returned no post for URL: " + resolved.originalUrl());
        }

        return post;
    }
}