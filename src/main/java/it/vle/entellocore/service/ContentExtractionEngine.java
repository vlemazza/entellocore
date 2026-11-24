package it.vle.entellocore.service;

import it.vle.entellocore.model.TextExtractionOutcome;

import java.util.List;

public interface ContentExtractionEngine {

    List<TextExtractionOutcome> extractFromText(String text);

    default boolean hasSupportedUrls(String text) {
        SocialUrlResolver resolver = new SocialUrlResolver();
        return SocialUrlExtractor.extractUrls(text).stream()
                .anyMatch(url -> resolver.resolve(url).isPresent());
    }
}