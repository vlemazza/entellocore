package it.vle.entellocore.service;

import it.vle.entellocore.exception.ContentExtractionException;

public interface FacebookDescriptionEnricher {
    String extractDescription(String url) throws ContentExtractionException, InterruptedException;
}